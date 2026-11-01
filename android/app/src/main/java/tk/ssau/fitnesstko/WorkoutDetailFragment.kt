package tk.ssau.fitnesstko

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentWorkoutDetailBinding
import tk.ssau.fitnesstko.model.dto.ExerciseDto
import tk.ssau.fitnesstko.model.dto.PersonSvgDto
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto

class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var exerciseAdapter: ExerciseDetailAdapter
    private var workoutId: Long = -1L

    private lateinit var workoutExercises: List<WorkoutExerciseDto>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutId = arguments?.getLong("workoutId") ?: -1L

        setupToolbar()
        setupRecyclerView()
        loadWorkoutData()

        binding.btnStartWorkout.setOnClickListener {
            if (::workoutExercises.isInitialized && workoutExercises.isNotEmpty()) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, WorkoutExecutionFragment().apply {
                        arguments = Bundle().apply {
                            putParcelableArrayList("exercises", ArrayList(workoutExercises))
                            putString("workoutName", binding.tvWorkoutName.text.toString())
                        }
                    })
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Упражнения не загружены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseDetailAdapter(
            emptyList(),
            fragmentManager = parentFragmentManager
        )
        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseAdapter
        }
    }

    private fun loadWorkoutData() {
        showLoading(true)
        loadWorkoutDetails()
    }

    private fun loadWorkoutDetails() {
        ApiService.workoutService.getWorkoutById(workoutId).enqueue(object : Callback<WorkoutDto> {
            override fun onResponse(call: Call<WorkoutDto>, response: Response<WorkoutDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { workout ->
                        updateWorkoutUI(workout)
                        loadExercises()
                        loadSvgImage()
                    }
                } else {
                    showError("Ошибка загрузки тренировки")
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<WorkoutDto>, t: Throwable) {
                showError("Ошибка соединения")
                showLoading(false)
            }
        })
    }

    private fun updateWorkoutUI(workout: WorkoutDto) {
        binding.apply {
            tvWorkoutName.text = workout.name
            tvDescription.text = workout.description
            tvDifficulty.text = workout.difficult
        }
    }

    private fun loadExercises() {
        ApiService.workoutExerciseService.getWorkoutExercises(workoutId)
            .enqueue(object : Callback<List<WorkoutExerciseDto>> {
                override fun onResponse(
                    call: Call<List<WorkoutExerciseDto>>,
                    response: Response<List<WorkoutExerciseDto>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { exercises ->
                            workoutExercises = exercises
                            loadExerciseDetails(exercises)
                        }
                    }
                }

                override fun onFailure(call: Call<List<WorkoutExerciseDto>>, t: Throwable) {
                    showError("Ошибка загрузки упражнений")
                }
            })
    }

    private fun loadExerciseDetails(workoutExercises: List<WorkoutExerciseDto>) {
        val exercises = mutableListOf<ExerciseDto>()
        workoutExercises.forEach { workoutExercise ->
            ApiService.exerciseService.getExerciseDetails(workoutExercise.exerciseId)
                .enqueue(object : Callback<ExerciseDto> {
                    override fun onResponse(
                        call: Call<ExerciseDto>,
                        response: Response<ExerciseDto>
                    ) {
                        response.body()?.let { exercise ->
                            exercises.add(exercise)
                            if (exercises.size == workoutExercises.size) {
                                exerciseAdapter.updateExercises(exercises)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ExerciseDto>, t: Throwable) {
                        showError("Ошибка загрузки деталей упражнения")
                    }
                })
        }
    }

    private fun loadSvgImage() {
        ApiService.workoutService.getWorkoutSvg(workoutId, "MALE")
            .enqueue(object : Callback<PersonSvgDto> {
                override fun onResponse(
                    call: Call<PersonSvgDto>,
                    response: Response<PersonSvgDto>
                ) {
                    response.body()?.front?.let { svgContent ->
                        try {
                            val svg = SVG.getFromString(svgContent)
                            binding.ivMuscleDiagram.setImageDrawable(
                                PictureDrawable(svg.renderToPicture())
                            )
                        } catch (_: SVGParseException) {
                            binding.ivMuscleDiagram.setImageResource(R.drawable.cat)
                        }
                    }
                }

                override fun onFailure(call: Call<PersonSvgDto>, t: Throwable) {
                    binding.ivMuscleDiagram.setImageResource(R.drawable.cat)
                }
            })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        binding.tvError.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}