package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.ProfileBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentProfile : Fragment(R.layout.profile) {

    private lateinit var binding: ProfileBinding
    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)
        prefs = (activity as MainActivity).prefs

        loadUserData()

        binding.btnProfileChangeFIO.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnProfileAddWeighing.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, AddWeightFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
        binding.btnProfileAddWeighing.text = "Добавить взвешивание"
    }

    private fun loadUserData() {
        val name = prefs.getString("user_name", "")
        val birthDay = prefs.getString("birthDay", "")

        binding.fio.text = name.ifEmpty { "Имя Фамилия" }
        binding.age.text = calculateAge(birthDay)
    }

    private fun calculateAge(birthDate: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birthYear = dateFormat.parse(birthDate)?.year ?: return ""
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            "${currentYear - birthYear} лет"
        } catch (_: Exception) {
            "Возраст"
        }
    }
}