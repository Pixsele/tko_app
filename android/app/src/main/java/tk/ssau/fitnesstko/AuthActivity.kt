package tk.ssau.fitnesstko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tk.ssau.fitnesstko.user.LoginFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_container, LoginFragment())
            .commit()
    }
}