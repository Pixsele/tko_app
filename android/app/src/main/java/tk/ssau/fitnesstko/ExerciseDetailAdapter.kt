package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tk.ssau.fitnesstko.databinding.ItemExerciseDetailBinding
import tk.ssau.fitnesstko.model.dto.ExerciseDto

class ExerciseDetailAdapter(
    private var exercises: List<ExerciseDto>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<ExerciseDetailAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(private val binding: ItemExerciseDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseDto) {
            binding.root.setOnClickListener {
                openExerciseDetails(exercise.id)
            }

            binding.tvExerciseName.text = exercise.name
            Glide.with(binding.root.context)
                .load("${ApiService.BASE_URL}api/exercise/image/${exercise.id}")
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.cat)
                .into(binding.ivExerciseImage)
        }

        private fun openExerciseDetails(exerciseId: Long?) {
            fragmentManager.beginTransaction()
                .replace(R.id.flFragment, ExerciseDetailFragment().apply {
                    arguments = Bundle().apply {
                        exerciseId?.toLong()?.let { putLong("exerciseId", it) }
                    }
                })
                .addToBackStack(null)
                .commit()
        }
    }

    fun updateExercises(newExercises: List<ExerciseDto>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size
}