package tk.ssau.fitnesstko

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Точка на графике с информацией о весе
 */
class CustomMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.data?.let { timestamp ->
            findViewById<TextView>(R.id.tvDate).text =
                SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
                    .format(Date(timestamp as Long))

            findViewById<TextView>(R.id.tvWeight).text =
                "%.1f кг".format(e.y)
        }
        super.refreshContent(e, highlight)
    }
}