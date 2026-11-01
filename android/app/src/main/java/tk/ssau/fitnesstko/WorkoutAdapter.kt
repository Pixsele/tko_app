package tk.ssau.fitnesstko

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.ItemWorkoutBinding
import tk.ssau.fitnesstko.model.dto.LikesWorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

class WorkoutAdapter(
    private var workouts: List<WorkoutForPageDto>,
    private val fragmentManager: FragmentManager,
    private val onDataUpdated: (WorkoutForPageDto) -> Unit,
    private val authManager: AuthManager
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutForPageDto) {
            binding.apply {
                tvWorkoutName.text = workout.name
                tvLikeCount.text = workout.likeCount?.toString() ?: "0"
                ivLike.setImageResource(
                    if (workout.liked) R.drawable.ic_liked
                    else R.drawable.ic_not_liked
                )

                ivLike.setOnClickListener {
                    handleLikeClick(workout)
                }

                root.setOnClickListener {
                    workout.id?.let { id -> openWorkoutDetails(id) }
                }
            }
        }

        private fun handleLikeClick(workout: WorkoutForPageDto) {
            val newLikedState = !workout.liked
            val newLikeCount = (workout.likeCount ?: 0) + if (newLikedState) 1 else -1

            val updatedWorkout = workout.copy(
                liked = newLikedState,
                likeCount = newLikeCount
            )

            updateItemInList(updatedWorkout)
            sendLikeRequest(workout, updatedWorkout)
        }

        private fun updateItemInList(updatedWorkout: WorkoutForPageDto) {
            val position = workouts.indexOfFirst { it.id == updatedWorkout.id }
            if (position != -1) {
                workouts = workouts.toMutableList().apply {
                    set(position, updatedWorkout)
                }
                notifyItemChanged(position)
                onDataUpdated(updatedWorkout)
            }
        }

        private fun sendLikeRequest(
            originalWorkout: WorkoutForPageDto,
            updatedWorkout: WorkoutForPageDto
        ) {
            val userId = authManager.getUserId() ?: run {
                Log.e("LikeError", "User ID not found")
                return
            }

            val likeDto = LikesWorkoutDto(
                id = null,
                userId = userId,
                workoutId = updatedWorkout.id!!
            )

            val call = if (updatedWorkout.liked) {
                ApiService.likesService.likeWorkout(likeDto)
            } else {
                ApiService.likesService.deleteLikeWorkout(likeDto)
            }

            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        Log.e("LikeError", "Failed to update like. Code: ${response.code()}")
                        revertLikeChange(originalWorkout)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("LikeError", "Network error: ${t.message}")
                    revertLikeChange(originalWorkout)
                }

                private fun revertLikeChange(original: WorkoutForPageDto) {
                    val position = workouts.indexOfFirst { it.id == original.id }
                    if (position != -1) {
                        workouts = workouts.toMutableList().apply {
                            set(position, original)
                        }
                        notifyItemChanged(position)
                    }
                }
            })
        }

        private fun openWorkoutDetails(workoutId: Long) {
            fragmentManager.beginTransaction()
                .replace(R.id.flFragment, WorkoutDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong("workoutId", workoutId)
                    }
                })
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workouts[position])
    }

    override fun getItemCount(): Int = workouts.size

    fun updateWorkouts(newWorkouts: List<WorkoutForPageDto>) {
        workouts = newWorkouts
        notifyDataSetChanged()
    }
}