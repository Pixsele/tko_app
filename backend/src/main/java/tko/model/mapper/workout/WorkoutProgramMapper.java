package tko.model.mapper.workout;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.WorkoutProgramEntity;
import tko.model.dto.workout.WorkoutProgramDTO;

@Mapper(componentModel = "spring")
public interface WorkoutProgramMapper {
    @Mapping(source = "workoutId", target = "workout.id")
    @Mapping(source = "programId", target = "program.id")
    WorkoutProgramEntity toEntity(WorkoutProgramDTO dto);

    @Mapping(source = "workout.id", target = "workoutId")
    @Mapping(source = "program.id",target = "programId")
    WorkoutProgramDTO toDTO(WorkoutProgramEntity entity);

    @Mapping(source = "workoutId", target = "workout.id")
    @Mapping(source = "programId", target = "program.id")
    @Mapping(target = "id", ignore = true)
    void updateEntity(WorkoutProgramDTO dto, @MappingTarget WorkoutProgramEntity entity);
}
