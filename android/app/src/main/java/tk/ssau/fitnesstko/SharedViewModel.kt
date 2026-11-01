package tk.ssau.fitnesstko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

class SharedViewModel : ViewModel() {
    private val _selectedExercises = MutableLiveData<List<ExerciseWithParams>>(emptyList())
    val selectedExercises: LiveData<List<ExerciseWithParams>> = _selectedExercises

    fun addExerciseWithParams(exercise: ExerciseForPageDto, sets: Int, reps: Int, rest: Int) {
        val currentList = _selectedExercises.value?.toMutableList() ?: mutableListOf()
        currentList.add(ExerciseWithParams(exercise, sets, reps, rest))
        _selectedExercises.value = currentList
    }

    fun clearSelectedExercises() {
        _selectedExercises.value = emptyList()
    }

    fun isExerciseSelected(exerciseId: Long?): Boolean {
        return _selectedExercises.value?.any { it.exercise.id == exerciseId } ?: false
    }
}

data class ExerciseWithParams(
    val exercise: ExerciseForPageDto,
    val sets: Int,
    val reps: Int,
    val rest: Int
)