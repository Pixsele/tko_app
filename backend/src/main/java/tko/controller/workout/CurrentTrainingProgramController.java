package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.CurrentTrainingProgramDTO;
import tko.service.workout.CurrentTrainingProgramService;

@RestController
@RequestMapping("/current-program")
public class CurrentTrainingProgramController {

    public final CurrentTrainingProgramService currentTrainingProgramService;

    @Autowired
    public CurrentTrainingProgramController(CurrentTrainingProgramService currentTrainingProgramService) {
        this.currentTrainingProgramService = currentTrainingProgramService;
    }

    @PostMapping
    public ResponseEntity<CurrentTrainingProgramDTO> createCurrentTrainingProgram(@RequestParam Integer period ,@Valid @RequestBody CurrentTrainingProgramDTO currentTrainingProgramDTO) {
        CurrentTrainingProgramDTO createProgram = currentTrainingProgramService.createCurrentTrainingProgram(period,currentTrainingProgramDTO);
        return new ResponseEntity<>(createProgram, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrentTrainingProgramDTO> readCurrentTrainingProgram(@PathVariable Long id) {
        CurrentTrainingProgramDTO readProgram = currentTrainingProgramService.readCurrentTrainingProgram(id);
        return new ResponseEntity<>(readProgram, HttpStatus.OK);
    }
}
