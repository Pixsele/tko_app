package tko.model.mapper.workout;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseDTO;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);
    ExerciseEntity toEntity(ExerciseDTO dto);
    ExerciseDTO toDto(ExerciseEntity entity);
}
