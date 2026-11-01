package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.LikesTrainingsProgramEntity;
import tko.model.dto.workout.LikesTrainingsProgramDTO;

@Mapper(componentModel = "spring")
public interface LikesTrainingsProgramMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "trainingsProgramId",target = "trainingsProgram.id")
    LikesTrainingsProgramEntity toEntity(LikesTrainingsProgramDTO dto);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "trainingsProgram.id", target = "trainingsProgramId")
    LikesTrainingsProgramDTO toDTO(LikesTrainingsProgramEntity entity);
}