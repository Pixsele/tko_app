package tk.ssau.fitnesstko.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.PagedResponse
import tk.ssau.fitnesstko.model.dto.ExerciseFilterOptionsDto
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

/**
 * Состояние каталога упражнений: фильтры, сортировка, постраничная загрузка.
 *
 * Любое изменение фильтра/сортировки ([updateFilter]) отменяет текущий запрос
 * и загружает список с первой страницы. Подгрузка следующих страниц — [loadNextPage].
 */
class ExerciseCatalogViewModel : ViewModel() {

    private val api = ApiService.exerciseService

    private val _filter = MutableLiveData(ExerciseCatalogFilter())
    val filter: LiveData<ExerciseCatalogFilter> = _filter

    private val _items = MutableLiveData<List<ExerciseForPageDto>>(emptyList())
    val items: LiveData<List<ExerciseForPageDto>> = _items

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _totalElements = MutableLiveData<Int?>(null)
    val totalElements: LiveData<Int?> = _totalElements

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _options = MutableLiveData(FALLBACK_OPTIONS)
    val options: LiveData<ExerciseFilterOptionsDto> = _options

    private var currentPage = -1
    private var isLastPage = false
    private var currentCall: Call<PagedResponse<ExerciseForPageDto>>? = null
    private var optionsLoaded = false
    private var initialLoaded = false

    /** Первая загрузка при открытии экрана (повторно не вызывается при повороте). */
    fun start() {
        loadFilterOptions()
        if (!initialLoaded) {
            initialLoaded = true
            refresh()
        }
    }

    fun updateFilter(newFilter: ExerciseCatalogFilter) {
        if (newFilter == _filter.value) return
        _filter.value = newFilter
        refresh()
    }

    fun updateFilter(transform: (ExerciseCatalogFilter) -> ExerciseCatalogFilter) {
        updateFilter(transform(_filter.value ?: ExerciseCatalogFilter()))
    }

    fun setSort(sort: CatalogSort) = updateFilter { it.copy(sort = sort, direction = null) }

    fun toggleDirection() = updateFilter {
        val next = if (it.effectiveDirection == SortDirection.DESC) SortDirection.ASC else SortDirection.DESC
        it.copy(direction = next)
    }

    fun resetFilters() = updateFilter { it.resetPanel() }

    fun consumeError() {
        _error.value = null
    }

    /** Перезагрузить список с первой страницы. */
    fun refresh() {
        currentCall?.cancel()
        currentPage = -1
        isLastPage = false
        _items.value = emptyList()
        _totalElements.value = null
        loadPage(0)
    }

    /** Подгрузить следующую страницу (вызывается при прокрутке к концу списка). */
    fun loadNextPage() {
        if (_loading.value == true || isLastPage) return
        loadPage(currentPage + 1)
    }

    private fun loadPage(page: Int) {
        val f = _filter.value ?: ExerciseCatalogFilter()
        _loading.value = true

        val call = api.getCatalog(
            muscles = f.muscles.toList().takeIf { it.isNotEmpty() },
            matchAllMuscles = if (f.muscles.size > 1 && f.matchAllMuscles) true else null,
            difficulties = f.difficulties.toList().takeIf { it.isNotEmpty() },
            requiresEquipment = f.equipment.apiValue,
            minLikes = f.minLikes.takeIf { it > 0 },
            minUsage = f.minUsage.takeIf { it > 0 },
            name = f.name.trim().takeIf { it.isNotEmpty() },
            onlyLiked = if (f.onlyLiked) true else null,
            sort = f.sort.apiValue,
            direction = f.direction?.apiValue,
            page = page,
            size = PAGE_SIZE
        )
        currentCall = call

        call.enqueue(object : Callback<PagedResponse<ExerciseForPageDto>> {
            override fun onResponse(
                call: Call<PagedResponse<ExerciseForPageDto>>,
                response: Response<PagedResponse<ExerciseForPageDto>>
            ) {
                // Ответ на устаревший запрос (фильтр уже поменялся) — игнорируем
                if (call.isCanceled || call !== currentCall) return
                _loading.value = false

                val body = response.body()
                if (!response.isSuccessful || body == null) {
                    _error.value = when (response.code()) {
                        400 -> "Некорректные параметры фильтра"
                        401, 403 -> "Требуется авторизация"
                        else -> "Ошибка загрузки каталога (${response.code()})"
                    }
                    return
                }

                currentPage = body.page.number
                isLastPage = body.page.totalPages == 0 || currentPage >= body.page.totalPages - 1
                _totalElements.value = body.page.totalElements
                _items.value = if (page == 0) body.content else (_items.value.orEmpty() + body.content)
            }

            override fun onFailure(call: Call<PagedResponse<ExerciseForPageDto>>, t: Throwable) {
                if (call.isCanceled || call !== currentCall) return
                _loading.value = false
                _error.value = "Нет соединения с сервером"
            }
        })
    }

    private fun loadFilterOptions() {
        if (optionsLoaded) return
        api.getFilterOptions().enqueue(object : Callback<ExerciseFilterOptionsDto> {
            override fun onResponse(
                call: Call<ExerciseFilterOptionsDto>,
                response: Response<ExerciseFilterOptionsDto>
            ) {
                response.body()?.takeIf { response.isSuccessful }?.let {
                    optionsLoaded = true
                    _options.value = it
                }
            }

            // Без опций с сервера остаётся FALLBACK_OPTIONS — панель всё равно работает
            override fun onFailure(call: Call<ExerciseFilterOptionsDto>, t: Throwable) = Unit
        })
    }

    override fun onCleared() {
        currentCall?.cancel()
        super.onCleared()
    }

    companion object {
        const val PAGE_SIZE = 20

        private val FALLBACK_OPTIONS = ExerciseFilterOptionsDto(
            muscles = listOf(
                "CHEST", "BACK", "SHOULDERS", "BICEPS", "TRICEPS",
                "ABS", "GLUTES", "QUADRICEPS", "HAMSTRINGS", "CALVES"
            ),
            difficulties = listOf("EASY", "MEDIUM", "HARD"),
            sortOptions = CatalogSort.values().map { it.apiValue },
            directions = listOf("ASC", "DESC")
        )
    }
}
