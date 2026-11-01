package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.dto.workout.ExerciseForPageDTO;
import tko.service.workout.ExerciseService;


@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO createdExercise = exerciseService.create(exerciseDTO);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> readExercise(@PathVariable Long id) {
        ExerciseDTO exerciseDTO = exerciseService.read(id);
        return new ResponseEntity<>(exerciseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable Long id, @Valid @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO updatedExercise = exerciseService.update(id,exerciseDTO);
        return new ResponseEntity<>(updatedExercise, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExerciseDTO> deleteExercise(@PathVariable Long id) {
        ExerciseDTO deletedExercise = exerciseService.delete(id);
        return new ResponseEntity<>(deletedExercise, HttpStatus.OK);
    }

    @GetMapping("/page")
    public PagedModel<ExerciseForPageDTO> readPage(Pageable pageable) {
        Page<ExerciseForPageDTO> exerciseDTOList = exerciseService.readPageable(pageable);

        return new PagedModel<>(exerciseDTOList);
    }
}
