package tk.ssau.fitnesstko.model.dto

import java.time.LocalDate

data class PlannedWorkoutDto(
    val id: Long?,
    val userId: Long,
    val workoutId: Long,
    val date: LocalDate,
    val status: String?
)