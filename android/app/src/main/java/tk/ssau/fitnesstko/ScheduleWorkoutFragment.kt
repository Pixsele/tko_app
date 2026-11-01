package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScheduleWorkoutFragment : Fragment(R.layout.fragment_schedule_workout) {

    private var selectedDate: Long? = null
    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = (activity as MainActivity).prefs

        setupDatePicker(view)
        setupWorkoutSpinner(view)
        setupSaveButton(view)
    }

    private fun setupDatePicker(view: View) {
        val etDate = view.findViewById<TextInputEditText>(R.id.etDate)
        etDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { timestamp ->
                selectedDate = timestamp
                etDate.setText(formatDate(timestamp))
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun setupWorkoutSpinner(view: View) {
        val workouts = arrayOf(
            "Силовая тренировка",
            "Кардио",
            "Йога",
            "Кроссфит",
            "Плавание",
            "Стретчинг"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            workouts
        )

        val actvWorkout = view.findViewById<AutoCompleteTextView>(R.id.actvWorkout)
        actvWorkout.setAdapter(adapter)
        actvWorkout.setOnItemClickListener { _, _, position, _ ->
            actvWorkout.setText(workouts[position], false)
        }
    }

    private fun setupSaveButton(view: View) {
        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (validateInput()) {
                saveWorkout()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            selectedDate == null -> {
                showToast("Выберите дату тренировки")
                false
            }

            view?.findViewById<AutoCompleteTextView>(R.id.actvWorkout)?.text.isNullOrEmpty() -> {
                showToast("Выберите тип тренировки")
                false
            }

            else -> true
        }
    }

    private fun saveWorkout() {
        selectedDate?.let { date ->
            val workoutType =
                view?.findViewById<AutoCompleteTextView>(R.id.actvWorkout)?.text.toString()
            val formattedDate = formatDate(date)

            prefs.saveWorkout(formattedDate, workoutType)
        }
    }

    private fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}