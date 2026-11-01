package tk.ssau.fitnesstko.model.dto

/**
 * Информация о лайке пользователя для тренировки.
 * @param id Уникальный идентификатор записи (может быть null для новых объектов).
 * @param userId Идентификатор пользователя, поставившего лайк.
 * @param workoutId Идентификатор тренировки, которой поставлен лайк.
 */
data class LikesWorkoutDto(
    val id: Long?,
    val userId: Long,
    val workoutId: Long
)
