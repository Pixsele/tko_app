package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.model.mapper.workout.WorkoutExerciseMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutExerciseService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutExerciseMapper workoutExerciseMapper;

    @Autowired
    public WorkoutExerciseService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, WorkoutExerciseRepository workoutExerciseRepository, WorkoutExerciseMapper workoutExerciseMapper) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutExerciseMapper = workoutExerciseMapper;
    }

    public WorkoutExerciseDTO create(WorkoutExerciseDTO workoutExerciseDTO) {
        if (workoutExerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(workoutExerciseDTO.getExerciseOrder() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exercise order must be null");
        }

        if (!(workoutRepository.existsById(workoutExerciseDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        if (!(exerciseRepository.existsById(workoutExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExerciseId not found");
        }

        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseMapper.toEntity(workoutExerciseDTO);
        Integer count_of_exercise = workoutExerciseRepository.findAllByWorkout_Id(workoutExerciseDTO.getWorkoutId()).size();
        workoutExerciseEntity.setExerciseOrder(count_of_exercise);
        WorkoutExerciseEntity result = workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(result);
    }

    public WorkoutExerciseDTO read(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return workoutExerciseMapper.toDTO(workoutExerciseEntity);
    }

    public WorkoutExerciseDTO update(Long id, WorkoutExerciseDTO workoutExerciseDTO) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if (!(workoutRepository.existsById(workoutExerciseDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        if (!(exerciseRepository.existsById(workoutExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExerciseId not found");
        }

        if(workoutExerciseDTO.getExerciseOrder() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exercise order cannot change in this method");
        }

        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        workoutExerciseMapper.updateEntity(workoutExerciseDTO, workoutExerciseEntity);
        WorkoutExerciseEntity savedEntity = workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(savedEntity);
    }

    public WorkoutExerciseDTO delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        WorkoutExerciseEntity deleteEntity = workoutExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        workoutExerciseRepository.delete(deleteEntity);
        Integer count_of_exercise = deleteEntity.getExerciseOrder();

        List<WorkoutExerciseEntity> list = workoutExerciseRepository.findAllByWorkout_Id(deleteEntity.getWorkout().getId());

        for(WorkoutExerciseEntity workoutExerciseEntity : list) {
            if(workoutExerciseEntity.getExerciseOrder() >= count_of_exercise) {
                workoutExerciseEntity.setExerciseOrder(count_of_exercise);
                workoutExerciseRepository.save(workoutExerciseEntity);
                count_of_exercise++;
            }
        }


        return workoutExerciseMapper.toDTO(deleteEntity);
    }

    public List<WorkoutExerciseDTO> findAllByWorkoutId(Long workoutId) {
        if (workoutId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if (!(workoutRepository.existsById(workoutId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        List<WorkoutExerciseEntity> workoutExerciseEntityList = workoutExerciseRepository.findAllByWorkout_Id(workoutId);
        workoutExerciseEntityList.sort(Comparator.comparing(WorkoutExerciseEntity::getExerciseOrder));

        return workoutExerciseEntityList.stream().map(workoutExerciseMapper::toDTO).collect(Collectors.toList());
    }

}
