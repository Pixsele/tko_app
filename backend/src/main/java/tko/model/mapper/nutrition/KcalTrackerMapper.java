package tko.model.mapper.nutrition;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.nutrition.KcalTrackerEntity;
import tko.model.dto.nutrition.KcalTrackerDTO;

@Mapper(componentModel = "spring")
public interface KcalTrackerMapper {
    @Mapping(source = "user.id", target = "userId")
    KcalTrackerDTO toDto(KcalTrackerEntity entity);
    @Mapping(source = "userId", target = "user.id")
    KcalTrackerEntity toEntity(KcalTrackerDTO dto);
    void updateEntity(KcalTrackerDTO dto, @MappingTarget KcalTrackerEntity entity);
}
