package tk.ssau.fitnesstko.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.databinding.ItemCatalogExerciseBinding
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

/**
 * Карточка упражнения в каталоге: название, сложность, мышцы, рейтинг и популярность.
 * ListAdapter + DiffUtil — при смене фильтра/сортировки список обновляется без мерцания.
 */
class CatalogExerciseAdapter(
    private val onExerciseClick: (ExerciseForPageDto) -> Unit
) : ListAdapter<ExerciseForPageDto, CatalogExerciseAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemCatalogExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseForPageDto) = with(binding) {
            tvExerciseName.text = exercise.name.orEmpty()

            val difficulty = exercise.difficult?.let { ExerciseLabels.difficulty(it) }
            val muscles = exercise.muscularGroup.orEmpty()
                .take(3)
                .joinToString(", ") { ExerciseLabels.muscle(it) }
            val subtitle = listOfNotNull(difficulty, muscles.takeIf { it.isNotEmpty() })
                .joinToString(" · ")
            tvExerciseMeta.text = subtitle
            tvExerciseMeta.visibility = if (subtitle.isEmpty()) View.GONE else View.VISIBLE

            val likes = exercise.likeCount ?: 0
            val usage = exercise.usageCount ?: 0
            tvLikes.text = "${if (exercise.liked) "♥" else "♡"} $likes"
            tvUsage.text = "в тренировках: $usage"

            Glide.with(root.context)
                .load("${ApiService.BASE_URL}api/exercise/image/${exercise.id}")
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivExercise)

            root.setOnClickListener { onExerciseClick(exercise) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemCatalogExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ExerciseForPageDto>() {
            override fun areItemsTheSame(a: ExerciseForPageDto, b: ExerciseForPageDto) = a.id == b.id
            override fun areContentsTheSame(a: ExerciseForPageDto, b: ExerciseForPageDto) = a == b
        }
    }
}
