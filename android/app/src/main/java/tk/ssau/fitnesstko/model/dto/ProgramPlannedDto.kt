package tk.ssau.fitnesstko.model.dto

data class ProgramPlannedDto(
    val id: Long?,
    val currentTrainingProgramId: Long,
    val plannedWorkoutId: Long
)
