package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutProgramDTO;
import tko.service.workout.WorkoutProgramService;

import java.util.List;

@RestController
@RequestMapping("workout-program")
public class WorkoutProgramController {
    private final WorkoutProgramService workoutProgramService;

    @Autowired
    public WorkoutProgramController(WorkoutProgramService workoutProgramService) {
        this.workoutProgramService = workoutProgramService;
    }

    @PostMapping
    public ResponseEntity<WorkoutProgramDTO> createWorkoutProgram(@RequestBody WorkoutProgramDTO workoutProgramDTO) {
        WorkoutProgramDTO createdWorkoutProgram = workoutProgramService.create(workoutProgramDTO);
        return ResponseEntity.ok(createdWorkoutProgram);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutProgramDTO> readWorkoutProgram(@PathVariable Long id) {
        WorkoutProgramDTO workoutProgramDTO = workoutProgramService.read(id);
        return new ResponseEntity<>(workoutProgramDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutProgramDTO> updateWorkoutProgram(@PathVariable Long id,@Valid @RequestBody WorkoutProgramDTO workoutProgramDTO) {
        WorkoutProgramDTO updatedWorkoutProgram = workoutProgramService.update(id, workoutProgramDTO);
        return new ResponseEntity<>(updatedWorkoutProgram, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutProgramDTO> deleteWorkoutProgram(@PathVariable Long id) {
        WorkoutProgramDTO deletedWorkoutProgram = workoutProgramService.delete(id);
        return new ResponseEntity<>(deletedWorkoutProgram, HttpStatus.OK);
    }

    @GetMapping("/workout/{id}")
    public ResponseEntity<List<WorkoutProgramDTO>> readWorkoutProgramsByWorkoutId(@PathVariable Long id) {
        List<WorkoutProgramDTO> workoutProgramDTOS = workoutProgramService.findAllByProgramId(id);
        return new ResponseEntity<>(workoutProgramDTOS, HttpStatus.OK);
    }
}
