package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.model.dto.workout.WorkoutExerciseDTO;

@Mapper(componentModel = "spring")
public interface WorkoutExerciseMapper {
    @Mapping(source = "workoutId", target = "workout.id")
    @Mapping(source = "exerciseId", target = "exercise.id")
    WorkoutExerciseEntity toEntity(WorkoutExerciseDTO dto);

    @Mapping(source = "workout.id", target = "workoutId")
    @Mapping(source = "exercise.id", target = "exerciseId")
    WorkoutExerciseDTO toDTO(WorkoutExerciseEntity entity);

    @Mapping(source = "workoutId", target = "workout.id")
    @Mapping(source = "exerciseId", target = "exercise.id")
    @Mapping(target = "id", ignore = true)
    void updateEntity(WorkoutExerciseDTO dto, @MappingTarget WorkoutExerciseEntity entity);
}
