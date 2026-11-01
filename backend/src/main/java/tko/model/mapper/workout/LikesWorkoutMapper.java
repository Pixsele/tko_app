package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.LikesWorkoutEntity;
import tko.model.dto.workout.LikesWorkoutDTO;

@Mapper(componentModel = "spring")
public interface LikesWorkoutMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "workoutId",target = "workout.id")
    LikesWorkoutEntity toEntity(LikesWorkoutDTO dto);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "workout.id", target = "workoutId")
    LikesWorkoutDTO toDTO(LikesWorkoutEntity entity);
}
