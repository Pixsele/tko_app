package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.model.dto.workout.TrainingsProgramDTO;

@Mapper(componentModel = "spring")
public interface TrainingsProgramMapper {
    TrainingsProgramEntity toEntity(TrainingsProgramDTO trainingsProgramDTO);
    TrainingsProgramDTO toDto(TrainingsProgramEntity trainingsProgramEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(TrainingsProgramDTO trainingsProgramDTO,@MappingTarget TrainingsProgramEntity trainingsProgramEntity);
}
