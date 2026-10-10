package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.ProfileBinding

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
        val firstName = prefs.getFirstName()
        val lastName = prefs.getLastName()
        val age = prefs.getAge()

        binding.fio.text = if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            "Имя Фамилия"
        }

        binding.age.text = if (age.isNotEmpty()) {
            "$age лет"
        } else {
            "Возраст"
        }
    }
}