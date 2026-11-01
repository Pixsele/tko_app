package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.LikesTrainingsProgramEntity;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.LikesTrainingsProgramRepository;
import tko.database.repository.workout.TrainingsProgramRepository;
import tko.model.dto.workout.LikesTrainingsProgramDTO;
import tko.model.mapper.workout.LikesTrainingsProgramMapper;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LikesTrainingsProgramService {


    private final TrainingsProgramRepository trainingsProgramRepository;
    private final UsersRepository usersRepository;
    private final LikesTrainingsProgramRepository likesTrainingsProgramRepository;
    private final TrainingsProgramService trainingsProgramService;
    private final LikesTrainingsProgramMapper likesTrainingsProgramMapper;

    public LikesTrainingsProgramService(TrainingsProgramRepository trainingsProgramRepository, UsersRepository usersRepository, LikesTrainingsProgramRepository likesTrainingsProgramRepository, TrainingsProgramService trainingsProgramService, LikesTrainingsProgramMapper likesTrainingsProgramMapper) {
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.usersRepository = usersRepository;
        this.likesTrainingsProgramRepository = likesTrainingsProgramRepository;
        this.trainingsProgramService = trainingsProgramService;
        this.likesTrainingsProgramMapper = likesTrainingsProgramMapper;
    }

    public LikesTrainingsProgramDTO createLikesExercise(LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        if(likesTrainingsProgramDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(trainingsProgramRepository.existsById(likesTrainingsProgramDTO.getTrainingsProgramId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise does not exist");
        }

        if(!(usersRepository.existsById(likesTrainingsProgramDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if(likesTrainingsProgramRepository.existsByUser_IdAndTrainingsProgram_Id(likesTrainingsProgramDTO.getUserId(), likesTrainingsProgramDTO.getTrainingsProgramId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this training program by this user is already exists");
        }

        if(!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), usersRepository.findById(likesTrainingsProgramDTO.getUserId()).get().getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }

        trainingsProgramService.addLike(likesTrainingsProgramDTO.getTrainingsProgramId());
        LikesTrainingsProgramEntity likesTrainingsProgramEntity =  likesTrainingsProgramMapper.toEntity(likesTrainingsProgramDTO);
        likesTrainingsProgramEntity.setCreatedAt(LocalDateTime.now());
        likesTrainingsProgramRepository.save(likesTrainingsProgramEntity);
        return likesTrainingsProgramMapper.toDTO(likesTrainingsProgramEntity);
    }

    public LikesTrainingsProgramDTO readLikesTrainingsProgram(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesTrainingsProgramEntity likesTrainingsProgram = likesTrainingsProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return likesTrainingsProgramMapper.toDTO(likesTrainingsProgram);
    }

    public LikesTrainingsProgramDTO updateLikesTrainingsProgram(Long id,LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        UsersEntity usersEntity = usersRepository.findById(likesTrainingsProgramDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        TrainingsProgramEntity trainingsProgramEntity = trainingsProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training program does not exist"));

        if(likesTrainingsProgramRepository.existsByUser_IdAndTrainingsProgram_Id(likesTrainingsProgramDTO.getUserId(), likesTrainingsProgramDTO.getTrainingsProgramId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this training program by this user is already exists");
        }

        LikesTrainingsProgramEntity likesTrainingsProgramEntity = likesTrainingsProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        trainingsProgramService.addLike(likesTrainingsProgramEntity.getTrainingsProgram().getId());

        likesTrainingsProgramEntity.setUser(usersEntity);
        likesTrainingsProgramEntity.setTrainingsProgram(trainingsProgramEntity);

        trainingsProgramService.addLike(likesTrainingsProgramEntity.getTrainingsProgram().getId());
        return likesTrainingsProgramMapper.toDTO(likesTrainingsProgramEntity);
    }

    public LikesTrainingsProgramDTO deleteLikesTrainingsProgram(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesTrainingsProgramEntity likesTrainingsProgram = likesTrainingsProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        likesTrainingsProgramRepository.delete(likesTrainingsProgram);
        trainingsProgramService.removeLike(likesTrainingsProgram.getTrainingsProgram().getId());
        return likesTrainingsProgramMapper.toDTO(likesTrainingsProgram);
    }

    public Page<LikesTrainingsProgramDTO> readLikesTrainingsProgramsByUserId(Long id ,Pageable pageable) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        Page<LikesTrainingsProgramEntity> likesTrainingsProgramEntityPage = likesTrainingsProgramRepository.findByUser_Id(id,pageable);
        return likesTrainingsProgramEntityPage.map(likesTrainingsProgramMapper::toDTO);
    }

    public boolean isLikeOwner(Long likeId, String username) {
        return likesTrainingsProgramRepository.existsByUser_IdAndId(usersRepository.findByLogin(username).getId(), likeId);
    }
    
}
