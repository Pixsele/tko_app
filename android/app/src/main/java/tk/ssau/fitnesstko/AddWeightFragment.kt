package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.FragmentAddWeightBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Фрагмент для добавления и отображения веса пользователя.
 */
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

    /**
     * Настройка UI.
     * Отображение последнего сохранённого веса. Если данных нет, то выводится надпись "Нет предыдущих измерений"
     */
    private fun setupUI() {
        prefs.getWeights().lastOrNull()?.let { (timestamp, weight) ->
            binding.tvLastWeight.text = "Последнее: %.1f кг\n(%s)".format(
                weight,
                timestampFormat.format(Date(timestamp))
            )
        } ?: run {
            binding.tvLastWeight.text = "Нет предыдущих измерений"
        }
    }

    /**
     * Обработка кнопки сохранения
     */
    private fun setupListeners() {
        binding.btnSaveWeight.setOnClickListener {
            val input = binding.etWeight.text.toString()
            when {
                input.isBlank() -> showError("Введите значение веса")
                !isValidWeight(input) -> showError("Некорректный вес (пример: 72.5, диапозон(20, 120)")
                else -> saveWeight(input.toFloat())
            }
        }
    }

    /**
     * Валидация данных
     * @param input Вес, который ввёл пользователь
     */
    private fun isValidWeight(input: String): Boolean {
        return try {
            val weight = input.toFloat()
            weight in 20f..120f
        } catch (_: NumberFormatException) {
            false
        }
    }

    private fun showError(message: String) {
        binding.etWeight.error = message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Функция сохранения веса
     * @param weight Вес пользователя, который нужно сохранить
     */
    private fun saveWeight(weight: Float) {
        try {
            prefs.saveWeight(weight)

            parentFragmentManager.setFragmentResult("weight_updated", Bundle())

            parentFragmentManager.popBackStack()

        } catch (e: Exception) {
            showError("Ошибка сохранения: ${e.localizedMessage}")
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etWeight.text?.clear()
    }
}