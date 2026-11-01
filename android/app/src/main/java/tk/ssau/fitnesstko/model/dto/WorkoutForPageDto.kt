package tk.ssau.fitnesstko.model.dto

/**
 * Упрощённое представление тренировки для списков/превью.
 * @param id Уникальный идентификатор тренировки.
 * @param name Название тренировки.
 * @param likeCount Общее количество лайков.
 * @param liked Лайкнул ли текущий пользователь эту тренировку.
 */
data class WorkoutForPageDto(
    val id: Long?,
    val name: String?,
    val likeCount: Int?,
    val liked: Boolean
)
