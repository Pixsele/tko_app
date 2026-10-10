package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.FragmentAddWeightBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddWeightFragment : Fragment(R.layout.fragment_add_weight) {

    private lateinit var binding: FragmentAddWeightBinding
    private lateinit var prefs: PreferencesManager
    private val timestampFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddWeightBinding.bind(view)
        prefs = (activity as MainActivity).prefs

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Показать последнее измерение
        prefs.getWeights().lastOrNull()?.let { (timestamp, weight) ->
            binding.tvLastWeight.text = "Последнее: %.1f кг\n(%s)".format(
                weight,
                timestampFormat.format(Date(timestamp))
            )
        } ?: run {
            binding.tvLastWeight.text = "Нет предыдущих измерений"
        }
    }

    private fun setupListeners() {
        binding.btnSaveWeight.setOnClickListener {
            val input = binding.etWeight.text.toString()
            when {
                input.isBlank() -> showError("Введите значение веса")
                !isValidWeight(input) -> showError("Некорректный вес (пример: 72.5)")
                else -> saveWeight(input.toFloat())
            }
        }
    }

    private fun isValidWeight(input: String): Boolean {
        return try {
            val weight = input.toFloat()
            weight in 30f..300f // Проверка на реалистичный вес
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun showError(message: String) {
        binding.etWeight.error = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun saveWeight(weight: Float) {
        try {
            // Сохраняем с текущим временем
            prefs.saveWeight(weight)

            // Уведомляем родительский фрагмент
            parentFragmentManager.setFragmentResult("weight_updated", Bundle())

            // Закрываем фрагмент
            parentFragmentManager.popBackStack()

        } catch (e: Exception) {
            showError("Ошибка сохранения: ${e.localizedMessage}")
        }
    }

    override fun onResume() {
        super.onResume()
        // Очистить поле при каждом открытии
        binding.etWeight.text?.clear()
    }
}