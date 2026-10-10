package tk.ssau.fitnesstko.model.dto

data class ExerciseDto(
    val id: Long?,
    val instruction: String,
    val name: String,
    val difficult: String,
    val muscularGroup: String,
    val likeCount: Int?,
    val requestEquipment: Boolean,
    val type: String
)
