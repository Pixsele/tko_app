package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.model.mapper.workout.WorkoutExerciseMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервис управления упражнениями, привязанными к конкретной тренировке.
 * Дополнительно ведёт счётчик популярности упражнения (exercise.usage_count):
 * +1 при добавлении в тренировку, -1 при удалении, перенос при смене упражнения.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #create(WorkoutExerciseDTO)} — добавление нового упражнения в тренировку</li>
 *     <li>{@link #read(Long)} — получение упражнения по идентификатору</li>
 *     <li>{@link #update(Long, WorkoutExerciseDTO)} — обновление информации об упражнении</li>
 *     <li>{@link #delete(Long)} — удаление упражнения из тренировки</li>
 *     <li>{@link #findAllByWorkoutId(Long)} — получение всех упражнений для заданной тренировки</li>
 * </ul>
 */

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

    @Transactional
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

        Integer count_of_exercise = workoutExerciseRepository.countByWorkout_Id(workoutExerciseDTO.getWorkoutId());
        workoutExerciseEntity.setExerciseOrder(count_of_exercise);
        WorkoutExerciseEntity result = workoutExerciseRepository.save(workoutExerciseEntity);

        exerciseRepository.incrementUsageCount(workoutExerciseDTO.getExerciseId());

        return workoutExerciseMapper.toDTO(result);
    }

    public WorkoutExerciseDTO read(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return workoutExerciseMapper.toDTO(workoutExerciseEntity);
    }

    @Transactional
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
        Long oldExerciseId = workoutExerciseEntity.getExercise() != null ? workoutExerciseEntity.getExercise().getId() : null;

        workoutExerciseMapper.updateEntity(workoutExerciseDTO, workoutExerciseEntity);
        WorkoutExerciseEntity savedEntity = workoutExerciseRepository.save(workoutExerciseEntity);

        // Если в тренировке заменили упражнение — переносим популярность
        Long newExerciseId = workoutExerciseDTO.getExerciseId();
        if (!Objects.equals(oldExerciseId, newExerciseId)) {
            if (oldExerciseId != null) {
                exerciseRepository.decrementUsageCount(oldExerciseId);
            }
            exerciseRepository.incrementUsageCount(newExerciseId);
        }

        return workoutExerciseMapper.toDTO(savedEntity);
    }

    @Transactional
    public WorkoutExerciseDTO delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        WorkoutExerciseEntity deleteEntity = workoutExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        workoutExerciseRepository.delete(deleteEntity);
        Integer count_of_exercise = deleteEntity.getExerciseOrder();

        List<WorkoutExerciseEntity> list = workoutExerciseRepository.findAllByWorkout_Id(deleteEntity.getWorkout().getId());
        list.sort(Comparator.comparing(WorkoutExerciseEntity::getExerciseOrder));

        for(WorkoutExerciseEntity workoutExerciseEntity : list) {
            if(workoutExerciseEntity.getExerciseOrder() >= count_of_exercise) {
                workoutExerciseEntity.setExerciseOrder(count_of_exercise);
                workoutExerciseRepository.save(workoutExerciseEntity);
                count_of_exercise++;
            }
        }

        if (deleteEntity.getExercise() != null) {
            exerciseRepository.decrementUsageCount(deleteEntity.getExercise().getId());
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
