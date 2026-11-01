package tk.ssau.fitnesstko.model.dto

import java.time.LocalDateTime

data class ExerciseMediaDto(
    val id: Long?,
    val urlImage: String?,
    val urlVideo: String?,
    val imageUpdated: LocalDateTime?,
    val videoUpdated: LocalDateTime?
)
