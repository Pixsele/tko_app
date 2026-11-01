package tk.ssau.fitnesstko.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Полное представление упражнения.
 * @param id Уникальный идентификатор упражнения (может быть null для новых объектов).
 * @param instruction Подробное описание техники выполнения упражнения.
 * @param name Название упражнения (например, "Приседания со штангой").
 * @param difficult Уровень сложности. Допустимые значения: "EASY", "MEDIUM", "HARD".
 * @param muscularGroup Целевая группа мышц (например, "Ноги", "Грудь", "Спина").
 * @param likeCount Количество лайков. Может быть null, если данные не загружены.
 * @param requestEquipment Требуется ли специальное оборудование (true/false).
 * @param type Тип упражнения (например, "Силовое", "Кардио", "Растяжка").
 */

@Parcelize
data class ExerciseDto(
    val id: Long?,
    val instruction: String,
    val name: String,
    val difficult: String,
    val muscularGroup: String,
    val likeCount: Int?,
    val requestEquipment: Boolean,
    val type: String
) : Parcelable
