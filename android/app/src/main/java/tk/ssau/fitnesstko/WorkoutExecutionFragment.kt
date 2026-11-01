package tk.ssau.fitnesstko

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import retrofit2.Call
import tk.ssau.fitnesstko.databinding.FragmentWorkoutExecutionBinding
import tk.ssau.fitnesstko.model.dto.ExerciseDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto
import retrofit2.Callback
import retrofit2.Response

class WorkoutExecutionFragment : Fragment(R.layout.fragment_workout_execution) {
    private lateinit var binding: FragmentWorkoutExecutionBinding
    private var exoPlayer: ExoPlayer? = null
    private var currentExerciseIndex = 0
    private var timer: CountDownTimer? = null
    private lateinit var exercises: List<WorkoutExerciseDto>
    private var isResting = false
    private var remainingSets: Int = 0
    private var currentExerciseRequest: Call<ExerciseDto>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutExecutionBinding.bind(view)

        exercises = arguments?.getParcelableArrayList("exercises") ?: emptyList()

        setupUI()
        setupClickListeners()
        initExerciseUI()
    }

    private fun setupUI() {
        binding.tvWorkoutName.text = arguments?.getString("workoutName") ?: "Тренировка"
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnPrev.setOnClickListener {
            if (currentExerciseIndex > 0) handleExerciseChange(currentExerciseIndex - 1)
        }

        binding.btnNext.setOnClickListener {
            handleExerciseChange(currentExerciseIndex + 1)
        }

        binding.btnCounter.setOnClickListener {
            if (!isResting) handleSetCounterClick()
        }
    }

    private fun initExerciseUI() {
        val exercise = exercises.getOrNull(currentExerciseIndex)
        exercise?.let {
            remainingSets = it.sets
            updateUIComponents(it)
            loadVideo(it.exerciseId)
        }
    }

    private fun updateUIComponents(exercise: WorkoutExerciseDto) {
        binding.tvExerciseName.text = "Загрузка..."
        binding.tvSets.text = remainingSets.toString()
        binding.tvTimer.text = "00:00"

        loadExerciseName(exercise.exerciseId)
        loadVideo(exercise.exerciseId)
    }

    private fun loadExerciseName(exerciseId: Long) {
        currentExerciseRequest?.cancel()

        currentExerciseRequest = ApiService.exerciseService.getExerciseDetails(exerciseId)
        currentExerciseRequest?.enqueue(object : Callback<ExerciseDto> {
            override fun onResponse(call: Call<ExerciseDto>, response: Response<ExerciseDto>) {
                if (response.isSuccessful && isAdded) {
                    response.body()?.let {
                        binding.tvExerciseName.text = it.name
                    }
                }
            }

            override fun onFailure(call: Call<ExerciseDto>, t: Throwable) {
                if (isAdded) {
                    binding.tvExerciseName.text = "Упражнение $exerciseId"
                }
            }
        })
    }


    @OptIn(UnstableApi::class)
    private fun loadVideo(exerciseId: Long) {
        val videoUrl = "${ApiService.BASE_URL}api/exercise/video/$exerciseId"
        val dataSourceFactory = OkHttpDataSource.Factory(ApiService.client)

        val mediaItem = MediaItem.fromUri(videoUrl)

        val player = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(ProgressiveMediaSource.Factory(dataSourceFactory))
            .build()

        binding.playerView.player = player
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    private fun handleSetCounterClick() {
        if (remainingSets > 0) {
            remainingSets--
            binding.tvSets.text = remainingSets.toString()

            if (remainingSets == 0) {
                startRestTimer(exercises[currentExerciseIndex].restTime * 1000L)
            }
        }
    }

    private fun startRestTimer(duration: Long) {
        isResting = true
        timer?.cancel()
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millis: Long) {
                binding.tvTimer.text = millis.toTimeFormat()
            }

            override fun onFinish() {
                isResting = false
                binding.tvTimer.text = "00:00"
                handleExerciseChange(currentExerciseIndex + 1)
            }
        }.start()
    }

    private fun handleExerciseChange(newIndex: Int) {
        timer?.cancel()
        isResting = false
        currentExerciseRequest?.cancel()

        when {
            newIndex < 0 -> return
            newIndex >= exercises.size -> exitWorkout()
            else -> {
                currentExerciseIndex = newIndex
                remainingSets = exercises[currentExerciseIndex].sets
                updateUIComponents(exercises[currentExerciseIndex])
            }
        }
    }
    private fun exitWorkout() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun Long.toTimeFormat() = String.format("%02d:%02d",
        (this / 1000) / 60,
        (this / 1000) % 60
    )

    override fun onDestroyView() {
        super.onDestroyView()
        currentExerciseRequest?.cancel()
        exoPlayer?.release()
        timer?.cancel()
    }
}