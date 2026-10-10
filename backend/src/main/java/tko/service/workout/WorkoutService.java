package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.mapper.workout.WorkoutMapper;

import java.time.LocalDateTime;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }

    public WorkoutDTO createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        workoutEntity.setCreatedAt(LocalDateTime.now());
        WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
        return workoutMapper.toDTO(saveEntity);
    }

    public WorkoutDTO readWorkoutById(@PathVariable Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return workoutMapper.toDTO(workoutEntity);
    }

    public WorkoutDTO updateWorkout(WorkoutDTO workoutDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        if(workoutRepository.existsById(workoutDTO.getId())) {
            WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
            return workoutMapper.toDTO(saveEntity);
        }
        throw new EntityNotFoundException("Workout not found");
    }

    public WorkoutDTO deleteWorkoutById(@PathVariable Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutEntity deleteEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        workoutRepository.delete(deleteEntity);
        return workoutMapper.toDTO(deleteEntity);
    }
}
