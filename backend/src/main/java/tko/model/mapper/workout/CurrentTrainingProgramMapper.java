package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.CurrentTrainingProgramEntity;
import tko.model.dto.workout.CurrentTrainingProgramDTO;

@Mapper(componentModel = "spring")
public interface CurrentTrainingProgramMapper {

    CurrentTrainingProgramEntity toEntity(CurrentTrainingProgramDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    CurrentTrainingProgramDTO toDto(CurrentTrainingProgramEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "trainingProgram", ignore = true)
    void updateEntity(CurrentTrainingProgramDTO dto, @MappingTarget CurrentTrainingProgramEntity entity);
}
