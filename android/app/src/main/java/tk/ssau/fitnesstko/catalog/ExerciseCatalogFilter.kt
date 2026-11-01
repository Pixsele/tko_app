package tk.ssau.fitnesstko.catalog

/**
 * Варианты сортировки каталога. [apiValue] совпадает с enum ExerciseSort на бэке.
 */
enum class CatalogSort(val apiValue: String, val title: String) {
    RATING("RATING", "Рейтинг"),
    POPULARITY("POPULARITY", "Популярность"),
    NAME("NAME", "Название"),
    NEWEST("NEWEST", "Новые");
}

/** Направление сортировки. null в фильтре = направление по умолчанию на бэке. */
enum class SortDirection(val apiValue: String) { ASC("ASC"), DESC("DESC") }

/** Фильтр по инвентарю: не важно / только с инвентарём / только без. */
enum class EquipmentFilter(val apiValue: Boolean?) { ANY(null), WITH(true), WITHOUT(false) }

/**
 * Текущее состояние фильтров каталога. Неизменяемое — новое состояние через copy().
 */
data class ExerciseCatalogFilter(
    val name: String = "",
    val muscles: Set<String> = emptySet(),
    val matchAllMuscles: Boolean = false,
    val difficulties: Set<String> = emptySet(),
    val equipment: EquipmentFilter = EquipmentFilter.ANY,
    val minLikes: Int = 0,
    val minUsage: Int = 0,
    val onlyLiked: Boolean = false,
    val sort: CatalogSort = CatalogSort.RATING,
    val direction: SortDirection? = null
) {
    /** Сколько фильтров панели активно (для подписи на кнопке «Фильтры»). */
    val activeFiltersCount: Int
        get() = muscles.size +
                difficulties.size +
                (if (equipment != EquipmentFilter.ANY) 1 else 0) +
                (if (minLikes > 0) 1 else 0) +
                (if (minUsage > 0) 1 else 0) +
                (if (onlyLiked) 1 else 0)

    /** Сбросить фильтры панели, оставив поиск и сортировку. */
    fun resetPanel(): ExerciseCatalogFilter = copy(
        muscles = emptySet(),
        matchAllMuscles = false,
        difficulties = emptySet(),
        equipment = EquipmentFilter.ANY,
        minLikes = 0,
        minUsage = 0,
        onlyLiked = false
    )

    /** Фактическое направление (как его выберет бэк, если direction == null). */
    val effectiveDirection: SortDirection
        get() = direction ?: if (sort == CatalogSort.NAME) SortDirection.ASC else SortDirection.DESC
}

/**
 * Русские подписи для enum'ов бэка. Если значения нет в словаре —
 * показывается само значение, так что новые мышцы на бэке ничего не ломают.
 */
object ExerciseLabels {
    private val muscles = mapOf(
        "CHEST" to "Грудь",
        "BACK" to "Спина",
        "LATS" to "Широчайшие",
        "TRAPEZIUS" to "Трапеции",
        "TRAPS" to "Трапеции",
        "SHOULDERS" to "Плечи",
        "DELTOIDS" to "Дельты",
        "BICEPS" to "Бицепс",
        "TRICEPS" to "Трицепс",
        "FOREARMS" to "Предплечья",
        "FOREARM" to "Предплечья",
        "ABS" to "Пресс",
        "OBLIQUES" to "Косые мышцы",
        "LOWER_BACK" to "Поясница",
        "GLUTES" to "Ягодицы",
        "QUADRICEPS" to "Квадрицепс",
        "QUADS" to "Квадрицепс",
        "HAMSTRINGS" to "Бицепс бедра",
        "CALVES" to "Икры",
        "ADDUCTORS" to "Приводящие",
        "NECK" to "Шея"
    )

    private val difficulties = mapOf(
        "EASY" to "Лёгкий",
        "MEDIUM" to "Средний",
        "HARD" to "Сложный"
    )

    private val types = mapOf(
        "STRENGTH" to "Силовое",
        "CARDIO" to "Кардио",
        "STRETCHING" to "Растяжка",
        "FLEXIBILITY" to "Гибкость",
        "BALANCE" to "Баланс"
    )

    fun muscle(value: String): String = muscles[value] ?: pretty(value)
    fun difficulty(value: String): String = difficulties[value] ?: pretty(value)
    fun type(value: String): String = types[value] ?: pretty(value)

    private fun pretty(value: String): String =
        value.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
}
