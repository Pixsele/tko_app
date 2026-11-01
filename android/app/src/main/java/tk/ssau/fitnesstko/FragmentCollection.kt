package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.CollectionBinding
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

class FragmentCollection(private val authManager: AuthManager) : Fragment() {

    private var _binding: CollectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter
    private var workouts = mutableListOf<WorkoutForPageDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        loadWorkouts()
        setupClickListeners()
    }

    private fun setupRecyclers() {
        val categories = listOf("Тренировки", "Программы")
        binding.rvCategories.apply {
            adapter = CategoriesAdapter(categories)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        workoutAdapter = WorkoutAdapter(
            workouts = emptyList(),
            fragmentManager = parentFragmentManager,
            onDataUpdated = { updatedWorkout ->
                (activity as? MainActivity)?.prefs?.updateLocalWorkout(updatedWorkout)
            },
            authManager = authManager
        )

        binding.rvWorkouts.apply {
            adapter = workoutAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.ibAddExercises.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    internal fun loadWorkouts() {

        ApiService.workoutService.getWorkouts()
            .enqueue(object : Callback<PagedResponse<WorkoutForPageDto>> {
                override fun onResponse(
                    call: Call<PagedResponse<WorkoutForPageDto>>,
                    response: Response<PagedResponse<WorkoutForPageDto>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { pagedResponse ->
                            updateWorkoutsList(
                                serverWorkouts = pagedResponse.content,
                                localWorkouts = getLocalWorkouts()
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<PagedResponse<WorkoutForPageDto>>, t: Throwable) {
                    updateWorkoutsList(localWorkouts = getLocalWorkouts())
                }
            })
    }

    private fun updateWorkoutsList(
        serverWorkouts: List<WorkoutForPageDto> = emptyList(),
        localWorkouts: List<WorkoutForPageDto> = emptyList()
    ) {
        workouts.clear()
        workouts.addAll(serverWorkouts + localWorkouts)
        workoutAdapter.updateWorkouts(workouts)
    }

    private fun handleLikeClick(workout: WorkoutForPageDto) {
        (activity as? MainActivity)?.prefs?.updateLocalWorkout(workout)
        updateWorkoutInList(workout)
    }

    private fun updateWorkoutInList(updatedWorkout: WorkoutForPageDto) {
        val position = workouts.indexOfFirst { it.id == updatedWorkout.id }
        if (position != -1) {
            workouts[position] = updatedWorkout
            workoutAdapter.notifyItemChanged(position)
        }
    }

    private fun getLocalWorkouts(): List<WorkoutForPageDto> {
        return (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}