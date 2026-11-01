package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.LikesTrainingsProgramDTO;
import tko.service.workout.LikesTrainingsProgramService;

@RestController
@RequestMapping("/like-program")
public class LikesTrainingsProgramController {

    private final LikesTrainingsProgramService likesTrainingsProgramService;

    @Autowired
    public LikesTrainingsProgramController(LikesTrainingsProgramService likesTrainingsProgramService) {
        this.likesTrainingsProgramService = likesTrainingsProgramService;
    }

    @PostMapping
    public ResponseEntity<LikesTrainingsProgramDTO> createLikesTrainingsProgram(@Valid @RequestBody LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        LikesTrainingsProgramDTO createdLike = likesTrainingsProgramService.createLikesExercise(likesTrainingsProgramDTO);
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@likesTrainingsProgramService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesTrainingsProgramDTO> readLikesTrainingsProgramById(@PathVariable Long id) {
        LikesTrainingsProgramDTO readLike = likesTrainingsProgramService.readLikesTrainingsProgram(id);
        return new ResponseEntity<>(readLike, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@likesTrainingsProgramService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesTrainingsProgramDTO> updateLikesTrainingsProgram(@PathVariable Long id, @Valid @RequestBody LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        LikesTrainingsProgramDTO updatedLike = likesTrainingsProgramService.updateLikesTrainingsProgram(id, likesTrainingsProgramDTO);
        return new ResponseEntity<>(updatedLike, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@likesTrainingsProgramService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesTrainingsProgramDTO> deleteLikesTrainingsProgram(@PathVariable Long id) {
        LikesTrainingsProgramDTO deletedLike = likesTrainingsProgramService.deleteLikesTrainingsProgram(id);
        return new ResponseEntity<>(deletedLike, HttpStatus.OK);
    }

    @GetMapping("/page/{id}")
    @PreAuthorize("@likesTrainingsProgramService.isLikeOwner(#id,authentication.name)")
    public PagedModel<LikesTrainingsProgramDTO> getLikesTrainingsPrograms(@PathVariable Long id, Pageable pageable) {
        Page<LikesTrainingsProgramDTO> likesTrainingsProgramDTOS = likesTrainingsProgramService.readLikesTrainingsProgramsByUserId(id, pageable);
        return new PagedModel<>(likesTrainingsProgramDTOS);
    }


}
