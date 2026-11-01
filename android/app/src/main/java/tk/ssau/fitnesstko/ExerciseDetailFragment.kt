package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentExerciseDetailBinding
import tk.ssau.fitnesstko.model.dto.ExerciseDto

class ExerciseDetailFragment : Fragment() {
    private var _binding: FragmentExerciseDetailBinding? = null
    private val binding get() = _binding!!
    private var exerciseId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseId = arguments?.getLong("exerciseId") ?: -1L

        setupToolbar()
        loadExerciseData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.toolbar.title = "Упражнение"
    }

    private fun loadExerciseData() {
        ApiService.exerciseService.getExerciseDetails(exerciseId).enqueue(
            object : Callback<ExerciseDto> {
                override fun onResponse(call: Call<ExerciseDto>, response: Response<ExerciseDto>) {
                    if (response.isSuccessful) {
                        response.body()?.let { exercise ->
                            updateUI(exercise)
                            loadVideo(exerciseId)
                            loadSvg(exerciseId)
                        }
                    }
                }

                override fun onFailure(call: Call<ExerciseDto>, t: Throwable) {
                    Toast.makeText(context, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun updateUI(exercise: ExerciseDto) {
        binding.apply {
            tvExerciseName.text = exercise.name
            tvDescription.text = exercise.instruction
            tvDifficulty.text = exercise.difficult
        }
    }

    @OptIn(UnstableApi::class)
    private fun loadVideo(exerciseId: Long) {
        val videoUrl = "${ApiService.BASE_URL}api/exercise/video/$exerciseId"

        val dataSourceFactory = OkHttpDataSource.Factory(ApiService.client)

        val mediaItem = MediaItem.fromUri(videoUrl)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        val player = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(ProgressiveMediaSource.Factory(dataSourceFactory))
            .build()

        binding.videoView.player = player
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    private fun loadSvg(exerciseId: Long) {
        val svgUrl = "${ApiService.BASE_URL}api/exercise/image/$exerciseId"

        Glide.with(this)
            .load(svgUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.cat)
            .into(binding.ivMuscleDiagram)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.player?.release()
        _binding = null
    }
}