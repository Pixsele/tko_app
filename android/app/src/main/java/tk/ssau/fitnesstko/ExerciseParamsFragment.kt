package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tk.ssau.fitnesstko.databinding.FragmentExerciseParamsBinding
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

class ExerciseParamsFragment : Fragment() {
    private lateinit var binding: FragmentExerciseParamsBinding
    private lateinit var viewModel: SharedViewModel
    private var exercise: ExerciseForPageDto? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseParamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        exercise = arguments?.getParcelable("selected_exercise") ?: return

        binding.btnSaveParams.setOnClickListener {
            saveParams()
        }
    }

    private fun saveParams() {
        val sets = binding.etSets.text.toString().toIntOrNull() ?: 0
        val reps = binding.etReps.text.toString().toIntOrNull() ?: 0
        val rest = binding.etRest.text.toString().toIntOrNull() ?: 0

        exercise?.let {
            viewModel.addExerciseWithParams(it, sets, reps, rest)
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateWorkoutFragment())
                .commit()
        }
    }
}