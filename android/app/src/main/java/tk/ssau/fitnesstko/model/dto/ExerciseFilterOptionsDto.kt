package tk.ssau.fitnesstko.model.dto

/**
 * Значения для панели фильтров каталога (GET api/exercise/filters).
 */
data class ExerciseFilterOptionsDto(
    val muscles: List<String> = emptyList(),
    val difficulties: List<String> = emptyList(),
    val types: List<String> = emptyList(),
    val sortOptions: List<String> = emptyList(),
    val directions: List<String> = emptyList()
)
