package tk.ssau.fitnesstko

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    enum class WorkoutState {
        NONE,
        PLANNED,    // Пунктирная серая рамка
        TODAY,      // Сплошная зеленая рамка
        COMPLETED   // Сплошная синяя заливка
    }

    var state: WorkoutState = WorkoutState.NONE
        set(value) {
            field = value
            updateAppearance()
        }

    // Цвета из ресурсов
    private val plannedColor = Color.parseColor("#9E9E9E") // Серый
    private val todayColor = Color.parseColor("#D6FFA4")   // Зеленый
    private val completedColor = Color.parseColor("#FFFFFF") // Синий

    init {
        setTextColor(Color.BLACK)
        textSize = 16f
        gravity = android.view.Gravity.CENTER
        setPadding(32, 32, 32, 32)
    }

    private fun updateAppearance() {
        background = when (state) {
            WorkoutState.PLANNED -> createDashedBorderDrawable(plannedColor)
            WorkoutState.TODAY -> createSolidBorderDrawable(todayColor)
            WorkoutState.COMPLETED -> createSolidBorderDrawable(completedColor)
            else -> null
        }
    }

    private fun createDashedBorderDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(
                3.dpToPx(),
                color,
                5f.dpToPx(),
                3f.dpToPx()
            ) // Пунктир: 10px длина, 5px промежуток
            setColor(Color.TRANSPARENT)
        }
    }

    private fun createSolidBorderDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(3.dpToPx(), color) // Сплошная линия
            setColor(Color.TRANSPARENT)
        }
    }

    private fun Float.dpToPx(): Float {
        return this * context.resources.displayMetrics.density
    }

    private fun Int.dpToPx(): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}