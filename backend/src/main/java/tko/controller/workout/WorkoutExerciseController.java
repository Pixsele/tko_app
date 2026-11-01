package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.service.workout.WorkoutExerciseService;

import java.util.List;

@RestController
@RequestMapping("/workout-exercise")
public class WorkoutExerciseController {
    private final WorkoutExerciseService workoutExerciseService;

    @Autowired
    public WorkoutExerciseController(WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseService = workoutExerciseService;
    }

    @PostMapping
    public ResponseEntity<WorkoutExerciseDTO> createWorkoutExercise(@Valid @RequestBody WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseDTO createdWorkoutExercise = workoutExerciseService.create(workoutExerciseDTO);
        return new ResponseEntity<>(createdWorkoutExercise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> readWorkoutExercise(@PathVariable Long id) {
        WorkoutExerciseDTO workoutExerciseDTO = workoutExerciseService.read(id);
        return new ResponseEntity<>(workoutExerciseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> updateWorkoutExercise(@PathVariable Long id,@Valid @RequestBody WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseDTO updatedWorkoutExercise = workoutExerciseService.update(id,workoutExerciseDTO);
        return new ResponseEntity<>(updatedWorkoutExercise, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> deleteWorkoutExercise(@PathVariable Long id) {
        WorkoutExerciseDTO deletedWorkoutExercise = workoutExerciseService.delete(id);
        return new ResponseEntity<>(deletedWorkoutExercise, HttpStatus.OK);
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<List<WorkoutExerciseDTO>> readWorkoutExercisesByWorkoutId(@PathVariable Long id) {
        List<WorkoutExerciseDTO> workoutExerciseDTOS = workoutExerciseService.findAllByWorkoutId(id);
        return new ResponseEntity<>(workoutExerciseDTOS, HttpStatus.OK);
    }
}
