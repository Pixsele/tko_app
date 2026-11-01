package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.model.dto.workout.LikesExerciseDTO;

@Mapper(componentModel = "spring")
public interface LikesExerciseMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "exerciseId",target = "exercise.id")
    LikesExerciseEntity toEntity(LikesExerciseDTO dto);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "exercise.id", target = "exerciseId")
    LikesExerciseDTO toDTO(LikesExerciseEntity entity);
}
