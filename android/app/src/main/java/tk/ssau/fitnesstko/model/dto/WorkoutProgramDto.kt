package tk.ssau.fitnesstko.model.dto

data class WorkoutProgramDto(
    val id: Long?,
    val programId: Long,
    val workoutId: Long,
    val workoutOrder: Int?
)
