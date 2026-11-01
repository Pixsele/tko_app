package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.PlannedWorkoutDTO;
import tko.service.workout.PlannedWorkoutService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planned-workout")
public class PlannedWorkoutController {

    private final PlannedWorkoutService plannedWorkoutService;

    @Autowired
    public PlannedWorkoutController(PlannedWorkoutService plannedWorkoutService) {
        this.plannedWorkoutService = plannedWorkoutService;
    }

    @PostMapping
    public ResponseEntity<PlannedWorkoutDTO> createPlannedWorkout(@Valid @RequestBody PlannedWorkoutDTO plannedWorkoutDTO) {
        PlannedWorkoutDTO createdPlannedWorkout = plannedWorkoutService.createPlannedWorkout(plannedWorkoutDTO);
        return new ResponseEntity<>(createdPlannedWorkout, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlannedWorkoutDTO> readPlannedWorkout(@PathVariable Long id) {
        PlannedWorkoutDTO readPlannedWorkout = plannedWorkoutService.readPlannedWorkout(id);
        return new ResponseEntity<>(readPlannedWorkout, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlannedWorkoutDTO> updatePlannedWorkout(@PathVariable Long id, @Valid @RequestBody PlannedWorkoutDTO plannedWorkoutDTO) {
        PlannedWorkoutDTO updatedPlannedWorkout = plannedWorkoutService.updatePlannedWorkout(id, plannedWorkoutDTO);
        return new ResponseEntity<>(updatedPlannedWorkout, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlannedWorkoutDTO> deletePlannedWorkout(@PathVariable Long id) {
        PlannedWorkoutDTO deletedPlannedWorkout = plannedWorkoutService.deletePlannedWorkout(id);
        return new ResponseEntity<>(deletedPlannedWorkout, HttpStatus.OK);
    }

    @GetMapping("/list-by-dates/{userId}")
    public ResponseEntity<List<PlannedWorkoutDTO>> readPlannedWorkoutsByDate(@PathVariable Long userId,
                                                                             @RequestParam LocalDate from,
                                                                             @RequestParam LocalDate to) {
        List<PlannedWorkoutDTO> plannedWorkoutDTOList = plannedWorkoutService.readByDate(userId, from, to);
        return new ResponseEntity<>(plannedWorkoutDTOList, HttpStatus.OK);
    }
}
