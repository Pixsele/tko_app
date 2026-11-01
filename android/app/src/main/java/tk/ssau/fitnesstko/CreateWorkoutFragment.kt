package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentCreateWorkoutBinding
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto

/**
 * Фрагмент для создания новой тренировки
 */
class CreateWorkoutFragment : Fragment() {

    private var _binding: FragmentCreateWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private var difficulty: String = "MEDIUM"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupUI()
        setupObservers()
        setupDifficultyGroup()
    }

    private fun setupUI() {
        binding.btnAddExercise.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ExercisePickerFragment())
                .addToBackStack("create_workout").commit()
        }

        binding.btnSave.setOnClickListener {
            if (validateForm()) {
                createWorkout()
            }
        }
    }

    private fun setupObservers() {
        viewModel.selectedExercises.observe(viewLifecycleOwner) { exercises ->
            binding.containerExercises.removeAllViews()
            exercises.forEach { exerciseWithParams ->
                val view = layoutInflater.inflate(
                    R.layout.item_selected_exercise_params,
                    binding.containerExercises,
                    false
                ).apply {
                    findViewById<TextView>(R.id.tvExerciseName).text =
                        exerciseWithParams.exercise.name
                    findViewById<TextView>(R.id.tvParams).text =
                        "Подходы: ${exerciseWithParams.sets}, Повторения: ${exerciseWithParams.reps}"
                }
                binding.containerExercises.addView(view)
            }
        }
    }

    private fun setupDifficultyGroup() {
        binding.rgDifficulty.setOnCheckedChangeListener { _, checkedId ->
            difficulty = when (checkedId) {
                R.id.rbEasy -> "EASY"
                R.id.rbMedium -> "MEDIUM"
                R.id.rbHard -> "HARD"
                else -> "MEDIUM"
            }
        }
    }

    private fun validateForm(): Boolean {
        return when {
            binding.etWorkoutName.text.isNullOrEmpty() -> {
                showError("Введите название тренировки")
                false
            }

            viewModel.selectedExercises.value.isNullOrEmpty() -> {
                showError("Добавьте хотя бы одно упражнение")
                false
            }

            else -> true
        }
    }

    private fun createWorkout() {
        val workoutDto = WorkoutDto(
            name = binding.etWorkoutName.text.toString(),
            description = binding.etDescription.text.toString(),
            difficult = difficulty,
            likeCount = null,
            id = null
        )

        ApiService.workoutService.createWorkout(workoutDto).enqueue(
            object : Callback<WorkoutDto> {
                override fun onResponse(call: Call<WorkoutDto>, response: Response<WorkoutDto>) {
                    if (response.isSuccessful) {
                        response.body()?.id?.let { workoutId ->
                            linkExercisesToWorkout(workoutId)
                        }
                    } else {
                        showError("Ошибка создания тренировки")
                    }
                }

                override fun onFailure(call: Call<WorkoutDto>, t: Throwable) {
                    showError("Ошибка сети: ${t.message}")
                }
            }
        )
    }

    private fun linkExercisesToWorkout(workoutId: Long) {
        viewModel.selectedExercises.value?.forEach { exerciseWithParams ->
            val dto = exerciseWithParams.exercise.id?.toLong()?.let {
                WorkoutExerciseDto(
                    workoutId = workoutId,
                    exerciseId = it,
                    sets = exerciseWithParams.sets,
                    reps = exerciseWithParams.reps,
                    distance = 0.0,
                    duration = 1.0,
                    id = null,
                    restTime = exerciseWithParams.rest,
                    exerciseOrder = null
                )
            }

            dto?.let { ApiService.workoutExerciseService.createWorkoutExercise(it) }?.enqueue(
                object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (!response.isSuccessful) {
                            showError("Ошибка привязки упражнений")
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        showError("Ошибка сети: ${t.message}")
                    }
                }
            )
        }

        parentFragmentManager.popBackStack()
        (activity as? MainActivity)?.refreshWorkouts()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearSelectedExercises()
    }
}