package tk.ssau.fitnesstko.model.dto

data class WorkoutExerciseDto(
    val id: Long?,
    val workoutId: Long,
    val exerciseId: Long,
    val sets: Int,
    val reps: Int,
    val distance: Double,
    val duration: Double,
    val restTime: Int,
    val exerciseOrder: Int?
)