package tko.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.user.WeightTrackerEntity;
import tko.model.dto.user.WeightTrackerDTO;

@Mapper(componentModel = "spring")
public interface WeightTrackerMapper {
    @Mapping(source = "user.id",target = "userId")
    WeightTrackerDTO toDto(WeightTrackerEntity entity);

    @Mapping(source = "userId", target = "user.id")
    WeightTrackerEntity toEntity(WeightTrackerDTO dto);
}
