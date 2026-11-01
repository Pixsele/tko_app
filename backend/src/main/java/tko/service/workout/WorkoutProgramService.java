package tko.service.workout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutProgramEntity;
import tko.database.repository.workout.TrainingsProgramRepository;
import tko.database.repository.workout.WorkoutProgramRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutProgramDTO;
import tko.model.mapper.workout.WorkoutProgramMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutProgramService {

    private final WorkoutProgramRepository workoutProgramRepository;
    private final WorkoutRepository workoutRepository;
    private final TrainingsProgramRepository trainingsProgramRepository;
    private final WorkoutProgramMapper workoutProgramMapper;

    @Autowired
    public WorkoutProgramService(WorkoutProgramRepository workoutProgramRepository, WorkoutRepository workoutRepository, TrainingsProgramRepository trainingsProgramRepository, WorkoutProgramMapper workoutProgramMapper) {
        this.workoutProgramRepository = workoutProgramRepository;
        this.workoutRepository = workoutRepository;
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.workoutProgramMapper = workoutProgramMapper;
    }

    public WorkoutProgramDTO create(WorkoutProgramDTO workoutProgramDTO) {
        if(workoutProgramDTO.getId() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Id must be null");
        }

        if(workoutProgramDTO.getWorkoutOrder() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Workout order must be null");
        }

        if(!(workoutRepository.existsById(workoutProgramDTO.getWorkoutId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout not found");
        }

        if(!(trainingsProgramRepository.existsById(workoutProgramDTO.getProgramId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainings program not found");
        }

        WorkoutProgramEntity workoutProgramEntity = workoutProgramMapper.toEntity(workoutProgramDTO);
        Integer count_of_workout = workoutProgramRepository.findAllByProgram_Id(workoutProgramDTO.getProgramId()).size();
        workoutProgramEntity.setWorkoutOrder(count_of_workout);
        WorkoutProgramEntity result = workoutProgramRepository.save(workoutProgramEntity);
        return workoutProgramMapper.toDTO(result);
    }

    public WorkoutProgramDTO read(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        WorkoutProgramEntity workoutProgramEntity = workoutProgramRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout Program not found"));
        return workoutProgramMapper.toDTO(workoutProgramEntity);
    }

    public WorkoutProgramDTO update(Long id,WorkoutProgramDTO workoutProgramDTO) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        if(!(workoutRepository.existsById(workoutProgramDTO.getWorkoutId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout not found");
        }

        if(!(trainingsProgramRepository.existsById(workoutProgramDTO.getProgramId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainings program not found");
        }

        if(workoutProgramDTO.getWorkoutOrder() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Workout order must be null");
        }

        WorkoutProgramEntity workoutProgramEntity = workoutProgramRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout Program not found"));
        workoutProgramMapper.updateEntity(workoutProgramDTO, workoutProgramEntity);
        WorkoutProgramEntity savedEntity = workoutProgramRepository.save(workoutProgramEntity);
        return workoutProgramMapper.toDTO(savedEntity);
    }

    public WorkoutProgramDTO delete(Long id) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        WorkoutProgramEntity deleteEntity = workoutProgramRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout Program not found"));
        workoutProgramRepository.delete(deleteEntity);

        Integer count_of_workout = workoutProgramRepository.findAllByProgram_Id(deleteEntity.getProgram().getId()).size();

        List<WorkoutProgramEntity> list = workoutProgramRepository.findAllByProgram_Id(deleteEntity.getProgram().getId());

        for(WorkoutProgramEntity workoutProgramEntity : list){
            if(workoutProgramEntity.getWorkoutOrder() >= count_of_workout){
                workoutProgramEntity.setWorkoutOrder(count_of_workout);
                workoutProgramRepository.save(workoutProgramEntity);
                count_of_workout++;
            }
        }

        return workoutProgramMapper.toDTO(deleteEntity);
    }

    public List<WorkoutProgramDTO> findAllByProgramId(Long programId) {
        if(programId == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        if(!(trainingsProgramRepository.existsById(programId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainings program not found");
        }

        List<WorkoutProgramEntity> workoutProgramEntityList = workoutProgramRepository.findAllByProgram_Id(programId);
        workoutProgramEntityList.sort(Comparator.comparing(WorkoutProgramEntity::getWorkoutOrder));

        return workoutProgramEntityList.stream().map(workoutProgramMapper::toDTO).collect(Collectors.toList());
    }
}
