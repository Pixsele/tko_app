package tk.ssau.fitnesstko

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tk.ssau.fitnesstko.catalog.CatalogExerciseAdapter
import tk.ssau.fitnesstko.catalog.CatalogSort
import tk.ssau.fitnesstko.catalog.ExerciseCatalogFilter
import tk.ssau.fitnesstko.catalog.ExerciseCatalogViewModel
import tk.ssau.fitnesstko.catalog.ExerciseFilterBottomSheet
import tk.ssau.fitnesstko.catalog.SortDirection
import tk.ssau.fitnesstko.databinding.FragmentExercisePickerBinding
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

/**
 * Каталог упражнений с фильтрами (мышцы, сложность, инвентарь, рейтинг, популярность),
 * поиском, сортировкой и бесконечной прокруткой.
 * Используется при создании тренировки: выбранные упражнения скрываются из списка.
 */
class ExercisePickerFragment : Fragment(R.layout.fragment_exercise_picker) {

    private var _binding: FragmentExercisePickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var catalogViewModel: ExerciseCatalogViewModel
    private lateinit var adapter: CatalogExerciseAdapter

    private val searchHandler = Handler(Looper.getMainLooper())
    private var pendingSearch: Runnable? = null

    /** Чтобы программная установка чипов сортировки не вызывала повторный запрос. */
    private var bindingSortChips = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExercisePickerBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        catalogViewModel = ViewModelProvider(this)[ExerciseCatalogViewModel::class.java]

        setupRecyclerView()
        setupSearch()
        setupSort()
        setupFilterButton()
        observeViewModel()

        catalogViewModel.start()
    }

    private fun setupRecyclerView() {
        adapter = CatalogExerciseAdapter { exercise -> openExerciseParamsFragment(exercise) }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.layoutManager = layoutManager
        binding.rvExercises.adapter = adapter

        binding.rvExercises.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (lastVisible >= adapter.itemCount - LOAD_MORE_THRESHOLD) {
                    catalogViewModel.loadNextPage()
                }
            }
        })

        binding.btnRetry.setOnClickListener { catalogViewModel.refresh() }
        binding.btnResetFilters.setOnClickListener {
            binding.etSearch.setText("")
            catalogViewModel.updateFilter { it.resetPanel().copy(name = "") }
        }
    }

    private fun setupSearch() {
        binding.etSearch.setText(catalogViewModel.filter.value?.name.orEmpty())

        binding.etSearch.doAfterTextChanged { text ->
            pendingSearch?.let { searchHandler.removeCallbacks(it) }
            val query = text?.toString().orEmpty()
            pendingSearch = Runnable {
                catalogViewModel.updateFilter { it.copy(name = query) }
            }.also { searchHandler.postDelayed(it, SEARCH_DEBOUNCE_MS) }
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                pendingSearch?.let { searchHandler.removeCallbacks(it) }
                catalogViewModel.updateFilter { it.copy(name = v.text.toString()) }
                true
            } else false
        }
    }

    private fun setupSort() {
        binding.cgSort.setOnCheckedStateChangeListener { _, checkedIds ->
            if (bindingSortChips) return@setOnCheckedStateChangeListener
            val sort = when (checkedIds.firstOrNull()) {
                R.id.chipSortPopularity -> CatalogSort.POPULARITY
                R.id.chipSortName -> CatalogSort.NAME
                R.id.chipSortNewest -> CatalogSort.NEWEST
                else -> CatalogSort.RATING
            }
            catalogViewModel.setSort(sort)
            binding.rvExercises.scrollToPosition(0)
        }

        binding.btnSortDirection.setOnClickListener {
            catalogViewModel.toggleDirection()
            binding.rvExercises.scrollToPosition(0)
        }
    }

    private fun setupFilterButton() {
        binding.btnFilters.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(ExerciseFilterBottomSheet.TAG) == null) {
                ExerciseFilterBottomSheet().show(childFragmentManager, ExerciseFilterBottomSheet.TAG)
            }
        }
    }

    private fun observeViewModel() {
        catalogViewModel.filter.observe(viewLifecycleOwner) { filter -> renderFilter(filter) }

        catalogViewModel.items.observe(viewLifecycleOwner) { exercises ->
            // Уже добавленные в тренировку упражнения не показываем
            val unselected = exercises.filter { !sharedViewModel.isExerciseSelected(it.id) }
            adapter.submitList(unselected)
            renderState()
        }

        catalogViewModel.loading.observe(viewLifecycleOwner) { renderState() }
        catalogViewModel.totalElements.observe(viewLifecycleOwner) { renderState() }

        catalogViewModel.error.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                catalogViewModel.consumeError()
                renderState(hasError = true)
            }
        }
    }

    private fun renderFilter(filter: ExerciseCatalogFilter) {
        val count = filter.activeFiltersCount
        binding.btnFilters.text = if (count > 0) "Фильтры ($count)" else "Фильтры"

        bindingSortChips = true
        binding.cgSort.check(
            when (filter.sort) {
                CatalogSort.RATING -> R.id.chipSortRating
                CatalogSort.POPULARITY -> R.id.chipSortPopularity
                CatalogSort.NAME -> R.id.chipSortName
                CatalogSort.NEWEST -> R.id.chipSortNewest
            }
        )
        bindingSortChips = false

        val desc = filter.effectiveDirection == SortDirection.DESC
        binding.btnSortDirection.text = if (desc) "↓" else "↑"
        binding.btnSortDirection.contentDescription =
            if (desc) "По убыванию" else "По возрастанию"
    }

    private fun renderState(hasError: Boolean = false) {
        val loading = catalogViewModel.loading.value == true
        val isEmpty = adapter.itemCount == 0 && catalogViewModel.items.value.isNullOrEmpty()
        val total = catalogViewModel.totalElements.value

        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.tvResultCount.text = total?.let { "Найдено: $it" }.orEmpty()

        val showEmpty = !loading && isEmpty
        binding.layoutEmpty.visibility = if (showEmpty) View.VISIBLE else View.GONE
        if (showEmpty) {
            val filtered = (catalogViewModel.filter.value?.activeFiltersCount ?: 0) > 0 ||
                    catalogViewModel.filter.value?.name?.isNotBlank() == true
            binding.tvEmpty.text = when {
                hasError -> "Не удалось загрузить упражнения"
                filtered -> "Ничего не найдено по выбранным фильтрам"
                else -> "Каталог пуст"
            }
            binding.btnRetry.visibility = if (hasError) View.VISIBLE else View.GONE
            binding.btnResetFilters.visibility = if (filtered && !hasError) View.VISIBLE else View.GONE
        }
    }

    private fun openExerciseParamsFragment(exercise: ExerciseForPageDto) {
        parentFragmentManager.beginTransaction()
            .add(R.id.flFragment, ExerciseParamsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("selected_exercise", exercise)
                }
            })
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        pendingSearch?.let { searchHandler.removeCallbacks(it) }
        binding.rvExercises.clearOnScrollListeners()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 400L
        private const val LOAD_MORE_THRESHOLD = 5
    }
}
