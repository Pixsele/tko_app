package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.model.dto.user.Gender
import tk.ssau.fitnesstko.model.dto.user.RegisterUsersDTO
import tk.ssau.fitnesstko.user.LoginFragment
import java.time.LocalDate

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var authManager: AuthManager
    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        prefs = PreferencesManager(requireContext())

        view.findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            val name = view.findViewById<TextInputEditText>(R.id.etName).text.toString()
            val login = view.findViewById<TextInputEditText>(R.id.etLogin).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.etPassword).text.toString()
            val birthDate = view.findViewById<TextInputEditText>(R.id.etBirthDate).text.toString()
            val weight = view.findViewById<TextInputEditText>(R.id.etWeight).text.toString()
            val height = view.findViewById<TextInputEditText>(R.id.etHeight).text.toString()
            val gender = when (view.findViewById<RadioGroup>(R.id.rgGender).checkedRadioButtonId) {
                R.id.rbMale -> Gender.MALE
                R.id.rbFemale -> Gender.FEMALE
                else -> Gender.MALE
            }

            if (validateInputs(name, login, password, birthDate, weight, height)) {
                registerUser(
                    name = name,
                    login = login,
                    password = password,
                    birthDate = birthDate,
                    weight = weight.toDouble(),
                    height = height.toDouble(),
                    gender = gender
                )
            }
        }

        view.findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateInputs(
        name: String,
        login: String,
        password: String,
        birthDate: String,
        weight: String,
        height: String
    ): Boolean {
        return when {
            name.isBlank() -> {
                showError("Введите имя")
                false
            }

            login.isBlank() -> {
                showError("Введите логин")
                false
            }

            password.length < 6 -> {
                showError("Пароль должен быть не менее 6 символов")
                false
            }

            !isValidDate(birthDate) -> {
                showError("Некорректная дата (формат: ГГГГ-ММ-ДД)")
                false
            }

            !isValidWeight(weight) -> {
                showError("Вес должен быть от 20 до 300 кг")
                false
            }

            !isValidHeight(height) -> {
                showError("Рост должен быть от 50 до 250 см")
                false
            }

            else -> true
        }
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun isValidWeight(weight: String): Boolean {
        return try {
            val value = weight.toDouble()
            value in 20.0..300.0
        } catch (_: NumberFormatException) {
            false
        }
    }

    private fun isValidHeight(height: String): Boolean {
        return try {
            val value = height.toDouble()
            value in 50.0..250.0
        } catch (_: NumberFormatException) {
            false
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun registerUser(
        name: String,
        login: String,
        password: String,
        birthDate: String,
        weight: Double,
        height: Double,
        gender: Gender
    ) {
        val user = RegisterUsersDTO(
            name = name,
            login = login,
            password = password,
            birthDay = LocalDate.parse(birthDate),
            weight = weight,
            height = height,
            gender = gender
        )

        ApiService.authService.register(user).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    prefs.saveUserProfile(name, birthDate, weight, height)
                    parentFragmentManager.popBackStack()
                    Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.auth_container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

}