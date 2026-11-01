package tk.ssau.fitnesstko.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Упрощённое представление упражнения для списков/превью.
 * @param id Уникальный идентификатор упражнения.
 * @param name Название упражнения.
 * @param likeCount Общее количество лайков.
 * @param liked Лайкнул ли текущий пользователь это упражнение.
 */
@Parcelize
data class ExerciseForPageDto(
    val id: Long?,
    val name: String?,
    val likeCount: Int?,
    val liked: Boolean
) : Parcelable
