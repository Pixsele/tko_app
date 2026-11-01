package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.ProgramPlannedDTO;
import tko.service.workout.ProgramPlannedService;

@Service
public class ProgramPlannedController {

    private final ProgramPlannedService programPlannedService;

    @Autowired
    public ProgramPlannedController(ProgramPlannedService programPlannedService) {
        this.programPlannedService = programPlannedService;
    }

    @PostMapping
    public ResponseEntity<ProgramPlannedDTO> createProgramPlanned(@Valid @RequestBody ProgramPlannedDTO programPlannedDTO) {
        ProgramPlannedDTO createdPlannedWorkout = programPlannedService.createProgramPlanned(programPlannedDTO);
        return new ResponseEntity<>(createdPlannedWorkout, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramPlannedDTO> readProgramPlanned(@PathVariable Long id) {
        ProgramPlannedDTO readPlannedWorkout = programPlannedService.readProgramPlannedById(id);
        return new ResponseEntity<>(readPlannedWorkout, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramPlannedDTO> updateProgramPlanned(@PathVariable Long id,@Valid @RequestBody ProgramPlannedDTO programPlannedDTO) {
        ProgramPlannedDTO updatedPlannedWorkout = programPlannedService.updateProgramPlannedById(id, programPlannedDTO);
        return new ResponseEntity<>(updatedPlannedWorkout, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProgramPlannedDTO> deleteProgramPlanned(@PathVariable Long id) {
        ProgramPlannedDTO deletedPlannedWorkout = programPlannedService.deleteProgramPlannedById(id);
        return new ResponseEntity<>(deletedPlannedWorkout, HttpStatus.OK);
    }
}
