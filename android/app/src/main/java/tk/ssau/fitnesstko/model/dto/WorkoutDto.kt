package tk.ssau.fitnesstko.model.dto

/**
 * Полное представление тренировки.
 * @param id Уникальный идентификатор тренировки (может быть null для новых объектов).
 * @param description Описание тренировки.
 * @param name Название тренировки (например, "Силовая программа для новичков").
 * @param difficult Уровень сложности. Допустимые значения: "EASY", "MEDIUM", "HARD".
 * @param likeCount Количество лайков. Может быть null, если данные не загружены.
 */
data class WorkoutDto(
    val id: Long?,
    val description: String,
    val name: String,
    val difficult: String,
    val likeCount: Int?
)
