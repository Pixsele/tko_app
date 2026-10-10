package tk.ssau.fitnesstko.model.dto

data class WorkoutDto(
    val id: Long?,
    val description: String,
    val name: String,
    val difficult: String,
    val likeCount: Int?
)
