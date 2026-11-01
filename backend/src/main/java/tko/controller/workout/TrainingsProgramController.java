package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.TrainingsProgramDTO;
import tko.model.dto.workout.TrainingsProgramForPageDTO;
import tko.service.workout.TrainingsProgramService;

@RestController
@RequestMapping("/program")
public class TrainingsProgramController {

    private final TrainingsProgramService trainingsProgramService;

    @Autowired
    public TrainingsProgramController(TrainingsProgramService trainingsProgramService) {
        this.trainingsProgramService = trainingsProgramService;
    }

    @PostMapping
    public ResponseEntity<TrainingsProgramDTO> createTrainingProgram(@Valid @RequestBody TrainingsProgramDTO trainingsProgramDTO) {
        TrainingsProgramDTO createdTrainingsProgram = trainingsProgramService.createTrainingsProgram(trainingsProgramDTO);
        return new ResponseEntity<>(createdTrainingsProgram, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingsProgramDTO> getTrainingProgram(@PathVariable Long id) {
        TrainingsProgramDTO trainingsProgramDTO = trainingsProgramService.readTrainingsProgramById(id);
        return new ResponseEntity<>(trainingsProgramDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingsProgramDTO> updateTrainingProgram(@PathVariable Long id, @Valid @RequestBody TrainingsProgramDTO trainingsProgramDTO) {
        TrainingsProgramDTO updatedTrainingsProgram = trainingsProgramService.updateTrainingsProgram(id, trainingsProgramDTO);
        return new ResponseEntity<>(updatedTrainingsProgram, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TrainingsProgramDTO> deleteTrainingProgram(@PathVariable Long id) {
        TrainingsProgramDTO deletedTrainingsProgram = trainingsProgramService.deleteTrainingsProgram(id);
        return new ResponseEntity<>(deletedTrainingsProgram, HttpStatus.OK);
    }

    @GetMapping("/page")
    public PagedModel<TrainingsProgramForPageDTO> readTrainingProgramsPageable(Pageable pageable) {
        Page<TrainingsProgramForPageDTO> trainingsProgramDTOPage = trainingsProgramService.readTrainingsProgramPageable(pageable);

        return new PagedModel<>(trainingsProgramDTOPage);
    }

}
