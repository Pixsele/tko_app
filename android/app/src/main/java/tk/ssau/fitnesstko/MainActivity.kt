package tk.ssau.fitnesstko

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tk.ssau.fitnesstko.databinding.ActivityMainBinding
import tk.ssau.fitnesstko.model.dto.user.AuthRequest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: AuthManager
    internal lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthManager(this)
        prefs = PreferencesManager(this)
        ApiService.initialize(applicationContext)


        lifecycleScope.launch {
            try {
                handleAuthentication()
            } catch (e: Exception) {
                Log.e("MainActivity", "Auth error: ${e.message}")
                redirectToAuth()
            }
        }
    }

    private fun handleAuthentication() {
        if (authManager.getToken() != null) {
            initMainInterface()
            return
        }

        val savedLogin = authManager.getLogin()
        val savedPassword = authManager.getPassword()

        when {
            !savedLogin.isNullOrEmpty() && !savedPassword.isNullOrEmpty() -> {
                try {
                    val response = ApiService.authService.login(
                        AuthRequest(savedLogin, savedPassword)
                    ).execute()

                    if (response.isSuccessful) {
                        response.body()?.let {
                            authManager.saveUserData(it.token, it.userId)
                            initMainInterface()
                        }
                    } else {
                        redirectToAuth()
                    }
                } catch (_: Exception) {
                    redirectToAuth()
                }
            }

            authManager.getToken() != null -> {
                initMainInterface()
            }

            else -> {
                redirectToAuth()
            }
        }
    }

    private fun initMainInterface() {
        runOnUiThread {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupNavigation()
            loadDefaultFragment()
        }
    }

    private fun setupNavigation() {
        val profileFragment = FragmentProfile()
        val collectionFragment = FragmentCollection(authManager)
        val workoutFragment = FragmentWorkout()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miCollection -> replaceFragment(collectionFragment)
                R.id.miProfile -> replaceFragment(profileFragment)
                R.id.miWorkout -> replaceFragment(workoutFragment)
            }
            true
        }
    }

    private fun loadDefaultFragment() {
        replaceFragment(FragmentWorkout())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .commit()
    }

    fun refreshWorkouts() {
        supportFragmentManager.fragments.forEach {
            if (it is FragmentCollection) {
                it.loadWorkouts()
            }
        }
    }

    private fun redirectToAuth() {
        runOnUiThread {
            authManager.clearToken()
            authManager.clearCredentials()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}