package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.ProgramPlannedEntity;
import tko.model.dto.workout.ProgramPlannedDTO;

@Mapper(componentModel = "spring")
public interface ProgramPlannedMapper {

    ProgramPlannedEntity toEntity(ProgramPlannedDTO dto);

    @Mapping(source = "currentTrainingProgram.id", target = "currentTrainingProgramId")
    @Mapping(source = "plannedWorkout.id", target = "plannedWorkoutId")
    ProgramPlannedDTO toDto(ProgramPlannedEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentTrainingProgram", ignore = true)
    @Mapping(target = "plannedWorkout", ignore = true)
    void updateEntity(ProgramPlannedDTO dto, @MappingTarget ProgramPlannedEntity entity);
}
