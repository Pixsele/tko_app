package tko.service.workout;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.LikesWorkoutEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.LikesWorkoutRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.LikesWorkoutDTO;
import tko.model.mapper.workout.LikesWorkoutMapper;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LikesWorkoutService {
    private final LikesWorkoutRepository likesWorkoutRepository;
    private final LikesWorkoutMapper likesWorkoutMapper;
    private final WorkoutService workoutService;
    private final WorkoutRepository workoutRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public LikesWorkoutService(LikesWorkoutRepository likesWorkoutRepository, LikesWorkoutMapper likesWorkoutMapper, WorkoutService workoutService, WorkoutRepository workoutRepository, UsersRepository usersRepository) {
        this.likesWorkoutRepository = likesWorkoutRepository;
        this.likesWorkoutMapper = likesWorkoutMapper;
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
        this.usersRepository = usersRepository;
    }

    public LikesWorkoutDTO createLikesWorkout(LikesWorkoutDTO likesWorkoutDTO) {
        if(likesWorkoutDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");

        }

        if(!(workoutRepository.existsById(likesWorkoutDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout does not exist");
        }

        if(!(usersRepository.existsById(likesWorkoutDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if(likesWorkoutRepository.existsByUser_IdAndWorkout_Id(likesWorkoutDTO.getUserId(), likesWorkoutDTO.getWorkoutId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this workout by this user is already exists");
        }

        if(!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), usersRepository.findById(likesWorkoutDTO.getUserId()).get().getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }

        workoutService.addLike(likesWorkoutDTO.getWorkoutId());
        LikesWorkoutEntity likesWorkoutEntity = likesWorkoutMapper.toEntity(likesWorkoutDTO);
        likesWorkoutEntity.setCreatedAt(LocalDateTime.now());
        likesWorkoutRepository.save(likesWorkoutEntity);
        return likesWorkoutMapper.toDTO(likesWorkoutEntity);
    }

    public LikesWorkoutDTO readLikesWorkout(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesWorkoutEntity likesWorkoutEntity = likesWorkoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return likesWorkoutMapper.toDTO(likesWorkoutEntity);
    }

    @Transactional
    public LikesWorkoutDTO updateLikesWorkout(Long id, LikesWorkoutDTO likesWorkoutDTO) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        UsersEntity usersEntity = usersRepository.findById(likesWorkoutDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        WorkoutEntity workoutEntity = workoutRepository.findById(likesWorkoutDTO.getWorkoutId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout does not exist"));

        if(likesWorkoutRepository.existsByUser_IdAndWorkout_Id(likesWorkoutDTO.getUserId(), likesWorkoutDTO.getWorkoutId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this workout by this user is already exists");
        }

        LikesWorkoutEntity likesWorkoutEntity = likesWorkoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        workoutService.removeLike(likesWorkoutEntity.getWorkout().getId());

        likesWorkoutEntity.setUser(usersEntity);
        likesWorkoutEntity.setWorkout(workoutEntity);

        workoutService.addLike(likesWorkoutEntity.getWorkout().getId());
        return likesWorkoutMapper.toDTO(likesWorkoutEntity);
    }

    public LikesWorkoutDTO deleteLikesWorkout(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesWorkoutEntity likesWorkoutEntity = likesWorkoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        likesWorkoutRepository.delete(likesWorkoutEntity);
        workoutService.removeLike(likesWorkoutEntity.getWorkout().getId());
        return likesWorkoutMapper.toDTO(likesWorkoutEntity);
    }

    public Page<LikesWorkoutDTO> readLikesWorkoutByUserId(Long id, Pageable pageable) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        Page<LikesWorkoutEntity> likesWorkoutEntityPage = likesWorkoutRepository.findByUser_Id(id,pageable);
        return likesWorkoutEntityPage.map(likesWorkoutMapper::toDTO);
    }

    public boolean isLikeOwner(Long likeId, String username) {
        return likesWorkoutRepository.existsByUser_IdAndId(usersRepository.findByLogin(username).getId(), likeId);
    }

}
