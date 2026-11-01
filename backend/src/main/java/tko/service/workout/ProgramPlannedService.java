package tko.service.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ProgramPlannedEntity;
import tko.database.repository.workout.CurrentTrainingProgramRepository;
import tko.database.repository.workout.PlannedWorkoutRepository;
import tko.database.repository.workout.ProgramPlannedRepository;
import tko.database.repository.workout.TrainingsProgramRepository;
import tko.model.dto.workout.ProgramPlannedDTO;
import tko.model.mapper.workout.ProgramPlannedMapper;

@Service
public class ProgramPlannedService {

    private final ProgramPlannedRepository programPlannedRepository;
    private final TrainingsProgramRepository trainingsProgramRepository;
    private final PlannedWorkoutRepository plannedWorkoutRepository;
    private final ProgramPlannedMapper programPlannedMapper;
    private final CurrentTrainingProgramRepository currentTrainingProgramRepository;

    @Autowired
    public ProgramPlannedService(ProgramPlannedRepository programPlannedRepository, TrainingsProgramRepository trainingsProgramRepository, PlannedWorkoutRepository plannedWorkoutRepository, ProgramPlannedMapper programPlannedMapper, CurrentTrainingProgramRepository currentTrainingProgramRepository) {
        this.programPlannedRepository = programPlannedRepository;
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.plannedWorkoutRepository = plannedWorkoutRepository;
        this.programPlannedMapper = programPlannedMapper;
        this.currentTrainingProgramRepository = currentTrainingProgramRepository;
    }


    public ProgramPlannedDTO createProgramPlanned(ProgramPlannedDTO programPlannedDTO) {
        if(programPlannedDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(currentTrainingProgramRepository.existsById(programPlannedDTO.getCurrentTrainingProgramId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program does not exist");
        }

        if(!(plannedWorkoutRepository.existsById(programPlannedDTO.getPlannedWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Planned workout does not exist");
        }

        ProgramPlannedEntity programPlannedEntity = programPlannedMapper.toEntity(programPlannedDTO);
        programPlannedRepository.save(programPlannedEntity);
        return programPlannedMapper.toDto(programPlannedRepository.save(programPlannedEntity));
    }

    public ProgramPlannedDTO readProgramPlannedById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProgramPlannedEntity programPlannedEntity = programPlannedRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Program planned does not exist"));
        return programPlannedMapper.toDto(programPlannedEntity);
    }

    public ProgramPlannedDTO updateProgramPlannedById(Long id ,ProgramPlannedDTO programPlannedDTO) {
        if(programPlannedDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must be null");
        }

        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(!(currentTrainingProgramRepository.existsById(programPlannedDTO.getCurrentTrainingProgramId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program does not exist");
        }

        if(!(plannedWorkoutRepository.existsById(programPlannedDTO.getPlannedWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Planned workout does not exist");
        }

        ProgramPlannedEntity programPlannedEntity = programPlannedRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Program planned does not exist"));

        programPlannedMapper.updateEntity(programPlannedDTO, programPlannedEntity);
        programPlannedRepository.save(programPlannedEntity);
        return programPlannedMapper.toDto(programPlannedRepository.save(programPlannedEntity));
    }

    public ProgramPlannedDTO deleteProgramPlannedById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProgramPlannedEntity programPlannedEntity = programPlannedRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Program planned does not exist"));

        programPlannedRepository.delete(programPlannedEntity);
        return programPlannedMapper.toDto(programPlannedEntity);
    }
}
