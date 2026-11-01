package tk.ssau.fitnesstko.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Упрощённое представление упражнения для каталога/списков.
 * @param id Уникальный идентификатор упражнения.
 * @param name Название упражнения.
 * @param type Тип упражнения (enum ExerciseType на бэке).
 * @param difficult Уровень сложности (EASY / MEDIUM / HARD).
 * @param requiresEquipment Нужен ли инвентарь.
 * @param muscularGroup Задействованные группы мышц (enum Muscle на бэке).
 * @param likeCount Рейтинг — количество лайков.
 * @param usageCount Популярность — сколько раз добавлено в тренировки.
 * @param liked Лайкнул ли текущий пользователь это упражнение.
 * @param hasImage Есть ли у упражнения картинка.
 */
@Parcelize
data class ExerciseForPageDto(
    val id: Long?,
    val name: String?,
    val likeCount: Int?,
    val liked: Boolean,
    val type: String? = null,
    val difficult: String? = null,
    val requiresEquipment: Boolean? = null,
    val muscularGroup: List<String>? = null,
    val usageCount: Int? = null,
    val hasImage: Boolean? = null
) : Parcelable
