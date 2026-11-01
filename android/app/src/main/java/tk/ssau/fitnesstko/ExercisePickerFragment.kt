package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentExercisePickerBinding
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

class ExercisePickerFragment : Fragment(R.layout.fragment_exercise_picker) {

    private lateinit var binding: FragmentExercisePickerBinding
    private lateinit var viewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExercisePickerBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupRecyclerView()
        loadExercises()
    }

    private fun setupRecyclerView() {
        binding.rvExercises.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExerciseAdapter(emptyList()) { exercise ->
                openExerciseParamsFragment(exercise)
            }
        }
    }

    private fun loadExercises() {
        ApiService.exerciseService.getExercises().enqueue(
            object : Callback<PagedResponse<ExerciseForPageDto>> {
                override fun onResponse(
                    call: Call<PagedResponse<ExerciseForPageDto>>,
                    response: Response<PagedResponse<ExerciseForPageDto>>
                ) {
                    response.body()?.let { pagedResponse ->
                        updateExerciseList(pagedResponse.content)
                    }
                }

                override fun onFailure(
                    call: Call<PagedResponse<ExerciseForPageDto>>,
                    t: Throwable
                ) {
                    showError("Ошибка загрузки упражнений")
                }
            }
        )
    }

    private fun updateExerciseList(exercises: List<ExerciseForPageDto>) {
        val unselected = exercises.filter { exercise ->
            !viewModel.isExerciseSelected(exercise.id)
        }

        (binding.rvExercises.adapter as? ExerciseAdapter)?.apply {
            updateExercises(unselected)
        }
    }

    private fun openExerciseParamsFragment(exercise: ExerciseForPageDto) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, ExerciseParamsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("selected_exercise", exercise)
                }
            })
            .addToBackStack(null)
            .commit()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}