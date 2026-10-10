package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.WorkoutDTO;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    WorkoutEntity toEntity(WorkoutDTO dto);
    WorkoutDTO toDTO(WorkoutEntity entity);
}
