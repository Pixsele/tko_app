package tk.ssau.fitnesstko.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.AuthManager
import tk.ssau.fitnesstko.AuthResponse
import tk.ssau.fitnesstko.MainActivity
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.RegisterFragment
import tk.ssau.fitnesstko.model.dto.user.AuthRequest

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var authManager: AuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())

        view.findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener {
            val login = view.findViewById<TextInputEditText>(R.id.etLogin).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.etPassword).text.toString()

            if (validateInput(login, password)) {
                loginUser(login, password)
            }
        }
        view.findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            navigateToRegister()
        }
    }

    private fun validateInput(login: String, password: String): Boolean {
        if (login.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginUser(login: String, password: String) {
        val authRequest = AuthRequest(login, password)

        ApiService.authService.login(authRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        authManager.saveUserData(it.token, it.userId)
                        authManager.saveCredentials(login, password)
                        navigateToMain()
                    }
                } else {
                    Toast.makeText(context, "Ошибка входа", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToRegister() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.auth_container, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }
}