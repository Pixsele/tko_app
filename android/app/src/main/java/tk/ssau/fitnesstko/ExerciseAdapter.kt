package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tk.ssau.fitnesstko.databinding.ItemSelectedExerciseBinding
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

class ExerciseAdapter(
    private var exercises: List<ExerciseForPageDto>,
    private val onExerciseClick: (ExerciseForPageDto) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemSelectedExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseForPageDto) {
            with(binding) {
                tvExerciseName.text = exercise.name

                Glide.with(root.context)
                    .load("${ApiService.BASE_URL}api/exercise/image/${exercise.id}")
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.cat)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivExercise)

                root.setOnClickListener {
                    onExerciseClick(exercise)
                }
            }
        }
    }

    fun updateExercises(newExercises: List<ExerciseForPageDto>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemSelectedExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount() = exercises.size
}