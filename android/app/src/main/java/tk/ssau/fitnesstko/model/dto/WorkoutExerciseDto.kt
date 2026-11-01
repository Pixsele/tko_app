package tk.ssau.fitnesstko.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Текущее выполняемое упражнение
 * @param id Уникальный идентификатор связи (может быть null для новых объектов).
 * @param workoutId Идентификатор тренировки.
 * @param exerciseId Идентификатор упражнения.
 * @param sets Количество подходов (например, 3).
 * @param reps Количество повторений в подходе (например, 12).
 * @param distance Дистанция в метрах (для кардио, 0.0 если не применимо).
 * @param duration Продолжительность в минутах (для интервальных тренировок).
 * @param restTime Время отдыха между подходами в секундах.
 * @param exerciseOrder Порядок выполнения упражнения в тренировке (начиная с 0).
 */
@Parcelize
data class WorkoutExerciseDto(
    val id: Long?,
    val workoutId: Long,
    val exerciseId: Long,
    val sets: Int,
    val reps: Int,
    val distance: Double,
    val duration: Double,
    val restTime: Int,
    val exerciseOrder: Int?
) : Parcelable