package tko.service.workout;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.PlannedWorkoutEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.PlannedWorkoutRepository;
import tko.database.repository.workout.ProgramPlannedRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.PlannedWorkoutDTO;
import tko.model.mapper.workout.PlannedWorkoutMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlannedWorkoutService {

    private final PlannedWorkoutRepository plannedWorkoutRepository;
    private final PlannedWorkoutMapper plannedWorkoutMapper;
    private final UsersRepository usersRepository;
    private final WorkoutRepository workoutRepository;
    private final ProgramPlannedRepository programPlannedRepository;


    public PlannedWorkoutService(PlannedWorkoutRepository plannedWorkoutRepository, PlannedWorkoutMapper plannedWorkoutMapper, UsersRepository usersRepository, WorkoutRepository workoutRepository, ProgramPlannedRepository programPlannedRepository) {
        this.plannedWorkoutRepository = plannedWorkoutRepository;
        this.plannedWorkoutMapper = plannedWorkoutMapper;
        this.usersRepository = usersRepository;
        this.workoutRepository = workoutRepository;
        this.programPlannedRepository = programPlannedRepository;
    }


    public PlannedWorkoutDTO createPlannedWorkout(PlannedWorkoutDTO plannedWorkoutDTO) {
        if(plannedWorkoutDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(usersRepository.existsById(plannedWorkoutDTO.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if(!(workoutRepository.existsById(plannedWorkoutDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found");
        }

        PlannedWorkoutEntity plannedWorkoutEntity = plannedWorkoutMapper.toEntity(plannedWorkoutDTO);
        plannedWorkoutRepository.save(plannedWorkoutEntity);
        return plannedWorkoutMapper.toDto(plannedWorkoutEntity);
    }


    public PlannedWorkoutDTO readPlannedWorkout(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        PlannedWorkoutEntity plannedWorkoutEntity = plannedWorkoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        return plannedWorkoutMapper.toDto(plannedWorkoutEntity);
    }

    public PlannedWorkoutDTO updatePlannedWorkout(Long id,PlannedWorkoutDTO plannedWorkoutDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(plannedWorkoutDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id request must be null");
        }

        if(!(usersRepository.existsById(plannedWorkoutDTO.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if(!(workoutRepository.existsById(plannedWorkoutDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found");
        }

        PlannedWorkoutEntity plannedWorkoutEntity = plannedWorkoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        plannedWorkoutMapper.updateEntity(plannedWorkoutDTO, plannedWorkoutEntity);
        return plannedWorkoutMapper.toDto(plannedWorkoutEntity);
    }

    public PlannedWorkoutDTO deletePlannedWorkout(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        PlannedWorkoutEntity plannedWorkoutEntity = plannedWorkoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        if(programPlannedRepository.existsByPlannedWorkout_Id(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete planned workout");
        }

        plannedWorkoutRepository.delete(plannedWorkoutEntity);
        return plannedWorkoutMapper.toDto(plannedWorkoutEntity);
    }

    public List<PlannedWorkoutDTO> readByDate(Long userId, LocalDate startDate, LocalDate endDate) {
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId must not be null");
        }

        if(!(usersRepository.existsById(userId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date must not be null");
        }

        List<PlannedWorkoutEntity> plannedWorkoutEntityList = plannedWorkoutRepository.findAllByUser_IdAndDateBetween(userId, startDate, endDate);

        List<PlannedWorkoutDTO> plannedWorkoutDTOList = plannedWorkoutEntityList.stream()
                .map(entity -> new PlannedWorkoutDTO(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getWorkout().getId(),
                        entity.getDate(),
                        entity.getStatus().toString()
                ))
                .collect(Collectors.toList());

        if(plannedWorkoutDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No weight records found for the specified period");
        }

        return plannedWorkoutDTOList;
    }
}
