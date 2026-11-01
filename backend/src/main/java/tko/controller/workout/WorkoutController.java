package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.dto.workout.WorkoutForPageDTO;
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
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return new ResponseEntity<>(createdWorkout,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> readWorkout(@PathVariable Long id) {
        WorkoutDTO workoutDTO = workoutService.readWorkoutById(id);
        return new ResponseEntity<>(workoutDTO,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, @Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(id,workoutDTO);
        return new ResponseEntity<>(updatedWorkout,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutDTO> deleteWorkout(@PathVariable Long id) {
        WorkoutDTO deletedWorkout = workoutService.deleteWorkoutById(id);
        return new ResponseEntity<>(deletedWorkout,HttpStatus.OK);
    }

    @GetMapping("/page")
    public PagedModel<WorkoutForPageDTO> readWorkoutPage(Pageable pageable) {
        Page<WorkoutForPageDTO> workoutDTOPage = workoutService.readWorkoutWithPageable(pageable);

        return new PagedModel<>(workoutDTOPage);
    }
}
