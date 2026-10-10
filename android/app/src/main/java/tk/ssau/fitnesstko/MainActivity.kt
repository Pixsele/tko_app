package tk.ssau.fitnesstko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var prefs: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferencesManager(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ApiService.initialize(applicationContext)

        val profileFragment = FragmentProfile()
        val collectionFragment = FragmentCollection()
        val workoutFragment = FragmentWorkout()

        setCurrentFragment(workoutFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miCollection -> setCurrentFragment(collectionFragment)
                R.id.miProfile -> setCurrentFragment(profileFragment)
                R.id.miWorkout -> setCurrentFragment(workoutFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    fun refreshWorkouts() {
        supportFragmentManager.fragments.forEach {
            if (it is FragmentCollection) {
                it.loadWorkouts()
            }
        }
    }
}