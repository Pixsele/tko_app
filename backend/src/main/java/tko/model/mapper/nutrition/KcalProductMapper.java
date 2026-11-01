package tko.model.mapper.nutrition;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.nutrition.KcalProductEntity;
import tko.model.dto.nutrition.KcalProductDTO;

@Mapper(componentModel = "spring")
public interface KcalProductMapper {
    @Mapping(source = "kcalTracker.id", target = "kcalTrackerId")
    @Mapping(source = "product.id", target = "productId")
    KcalProductDTO toDto(KcalProductEntity entity);

    @Mapping(source = "kcalTrackerId",target = "kcalTracker.id")
    @Mapping(source = "productId", target = "product.id")
    KcalProductEntity toEntity(KcalProductDTO dto);

    void updateEntity(KcalProductDTO dto, @MappingTarget KcalProductEntity entity);
}
