package tko.model.mapper;

import tko.database.entity.user.UsersEntity;
import tko.model.dto.user.UsersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper userMapper = Mappers.getMapper(UsersMapper.class);

    UsersEntity toEntity(UsersDTO usersDTO);

    UsersDTO toDTO(UsersEntity usersEntity);
}
