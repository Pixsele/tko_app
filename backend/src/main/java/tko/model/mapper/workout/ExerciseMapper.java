package tko.model.mapper.workout;


import org.hibernate.mapping.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseDTO;
import tko.utils.personSVG.Muscle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseEntity toEntity(ExerciseDTO dto);
    ExerciseDTO toDto(ExerciseEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(ExerciseDTO dto,@MappingTarget ExerciseEntity entity);


    default List<Muscle> mapStringToMuscles(String string) {
        if(string == null || string.isEmpty()){
            return Collections.emptyList();
        }
        return Arrays.stream(string.split(",")).map(String::trim).map(Muscle::getById).collect(Collectors.toList());
    }

    default String mapMusclesToString(List<Muscle> muscles) {
        if(muscles == null || muscles.isEmpty()){
            return null ;
        }
        return muscles.stream().map(Muscle::getId).collect(Collectors.joining(","));
    }
}
