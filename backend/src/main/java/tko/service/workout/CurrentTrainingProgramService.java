package tko.service.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.CurrentTrainingProgramEntity;
import tko.database.entity.workout.PlannedWorkoutEntity;
import tko.database.entity.workout.ProgramPlannedEntity;
import tko.database.entity.workout.WorkoutProgramEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.*;
import tko.model.dto.workout.CurrentTrainingProgramDTO;
import tko.model.mapper.workout.CurrentTrainingProgramMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentTrainingProgramService {

    private final CurrentTrainingProgramRepository currentTrainingProgramRepository;
    private final UsersRepository usersRepository;
    private final TrainingsProgramRepository trainingsProgramRepository;
    private final WorkoutProgramRepository workoutProgramRepository;
    private final CurrentTrainingProgramMapper currentTrainingProgramMapper;
    private final ProgramPlannedRepository programPlannedRepository;
    private final PlannedWorkoutRepository plannedWorkoutRepository;

    @Autowired
    public CurrentTrainingProgramService(CurrentTrainingProgramRepository currentTrainingProgramRepository, UsersRepository usersRepository, TrainingsProgramRepository trainingsProgramRepository, WorkoutProgramRepository workoutProgramRepository, CurrentTrainingProgramMapper currentTrainingProgramMapper, ProgramPlannedRepository programPlannedRepository, PlannedWorkoutRepository plannedWorkoutRepository) {
        this.currentTrainingProgramRepository = currentTrainingProgramRepository;
        this.usersRepository = usersRepository;
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.workoutProgramRepository = workoutProgramRepository;
        this.currentTrainingProgramMapper = currentTrainingProgramMapper;
        this.programPlannedRepository = programPlannedRepository;
        this.plannedWorkoutRepository = plannedWorkoutRepository;
    }

    public CurrentTrainingProgramDTO createCurrentTrainingProgram(Integer period,CurrentTrainingProgramDTO currentTrainingProgramDTO) {
        if(currentTrainingProgramDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(period == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Period must be set");
        }

        if(!(usersRepository.existsById(currentTrainingProgramDTO.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User does not exist");
        }

        if(!(trainingsProgramRepository.existsById(currentTrainingProgramDTO.getTrainingProgramId()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Training program does not exist");
        }

        CurrentTrainingProgramEntity currentTrainingProgramEntity = currentTrainingProgramMapper.toEntity(currentTrainingProgramDTO);
        currentTrainingProgramRepository.save(currentTrainingProgramEntity);

        List<WorkoutProgramEntity> workoutList = workoutProgramRepository.findAllByProgram_Id(currentTrainingProgramDTO.getTrainingProgramId());

        LocalDate currentDate = LocalDate.now();

        List<PlannedWorkoutEntity> plannedWorkoutEntityList = new ArrayList<>();

        for(WorkoutProgramEntity workoutProgramEntity : workoutList) {

            PlannedWorkoutEntity plannedWorkoutEntity = new PlannedWorkoutEntity();
            plannedWorkoutEntity.setUser(currentTrainingProgramEntity.getUser());
            plannedWorkoutEntity.setWorkout(workoutProgramEntity.getWorkout());
            plannedWorkoutEntity.setDate(currentDate.plusDays(period));
            plannedWorkoutEntity.setStatus(PlannedWorkoutEntity.WorkoutStatus.PLANNED);
            plannedWorkoutEntityList.add(plannedWorkoutEntity);
        }

        List<ProgramPlannedEntity> programPlannedEntityList = new ArrayList<>();

        for(PlannedWorkoutEntity plannedWorkoutEntity : plannedWorkoutEntityList) {
            ProgramPlannedEntity programPlannedEntity = new ProgramPlannedEntity();
            programPlannedEntity.setCurrentTrainingProgram(currentTrainingProgramEntity);
            programPlannedEntity.setPlannedWorkout(plannedWorkoutEntity);
            programPlannedEntityList.add(programPlannedEntity);
        }

        UsersEntity user = usersRepository.findById(currentTrainingProgramDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "User does not exist"));

        user.setCurrentTrainingProgram(currentTrainingProgramEntity);
        usersRepository.save(user);

        programPlannedRepository.saveAll(programPlannedEntityList);
        plannedWorkoutRepository.saveAll(plannedWorkoutEntityList);

        currentTrainingProgramRepository.save(currentTrainingProgramEntity);
        return currentTrainingProgramMapper.toDto(currentTrainingProgramEntity);
    }

    public CurrentTrainingProgramDTO readCurrentTrainingProgram(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be set");
        }

        CurrentTrainingProgramEntity currentTrainingProgramEntity = currentTrainingProgramRepository
                .findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        return currentTrainingProgramMapper.toDto(currentTrainingProgramEntity);
    }
    //TODO Delete,update,get with percent
}
