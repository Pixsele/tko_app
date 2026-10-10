package tko.controller.workout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutDTO;
import tko.service.workout.WorkoutService;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return new ResponseEntity<>(createdWorkout,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> readWorkout(@PathVariable Long id) {
        WorkoutDTO workoutDTO = workoutService.readWorkoutById(id);
        return new ResponseEntity<>(workoutDTO,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WorkoutDTO> updateWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(workoutDTO);
        return new ResponseEntity<>(updatedWorkout,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<WorkoutDTO> deleteWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO deletedWorkout = workoutService.deleteWorkoutById(workoutDTO.getId());
        return new ResponseEntity<>(deletedWorkout,HttpStatus.OK);
    }


}
