package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = (activity as MainActivity).prefs

        val etFirstName = view.findViewById<TextInputEditText>(R.id.etFirstName)
        val etLastName = view.findViewById<TextInputEditText>(R.id.etLastName)
        val etAge = view.findViewById<TextInputEditText>(R.id.etAge)

        view.findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val age = etAge.text.toString()

            prefs.saveUserData(firstName, lastName, age)

            parentFragmentManager.popBackStack()
        }
    }
}