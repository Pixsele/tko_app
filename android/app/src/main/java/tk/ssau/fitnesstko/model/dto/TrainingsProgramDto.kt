package tk.ssau.fitnesstko.model.dto

data class TrainingsProgramDto(
    val id: Long?,
    val description: String,
    val name: String,
    val difficult: String,
    val likeCount: Int?
)
