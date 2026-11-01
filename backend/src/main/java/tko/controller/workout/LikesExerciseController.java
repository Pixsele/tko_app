package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.LikesExerciseDTO;
import tko.service.workout.LikesExerciseService;

import java.security.Security;

@RestController
@RequestMapping("/like-exercise")
public class LikesExerciseController {

    private final LikesExerciseService likesExerciseService;

    @Autowired
    public LikesExerciseController(LikesExerciseService likesExerciseService) {
        this.likesExerciseService = likesExerciseService;
    }

    @PostMapping
    public ResponseEntity<LikesExerciseDTO> createLikesExercise(@Valid @RequestBody LikesExerciseDTO likesExerciseDTO) {
        LikesExerciseDTO createdLike = likesExerciseService.createLikesExercise(likesExerciseDTO);
        return new ResponseEntity<>(createdLike,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@likesExerciseService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesExerciseDTO> readLikesExerciseById(@PathVariable Long id) {
        LikesExerciseDTO readLike = likesExerciseService.readLikesExercise(id);
        return new ResponseEntity<>(readLike,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@likesExerciseService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesExerciseDTO> updateLikeExercise(@PathVariable Long id, @Valid @RequestBody LikesExerciseDTO likesExerciseDTO) {
        LikesExerciseDTO updatedLike = likesExerciseService.updateLikesExercise(id, likesExerciseDTO);
        return new ResponseEntity<>(updatedLike,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@likesExerciseService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesExerciseDTO> deleteLikesExercise(@PathVariable Long id) {
        LikesExerciseDTO deletedLike = likesExerciseService.deleteLikesExercise(id);
        return new ResponseEntity<>(deletedLike, HttpStatus.OK);
    }

    @GetMapping("/page/{id}")
    @PreAuthorize("@likesExerciseService.isLikeOwner(#id,authentication.name)")
    public PagedModel<LikesExerciseDTO> getLikesExercisesByUserId(@PathVariable Long id, Pageable pageable) {
        Page<LikesExerciseDTO> likesExerciseDTOS = likesExerciseService.readLikesExerciseByUserId(id,pageable);
        return new PagedModel<>(likesExerciseDTOS);
    }
}
