package tko.model.mapper;

import tko.database.entity.user.UsersEntity;
import tko.model.dto.user.UsersDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersEntity toEntity(UsersDTO usersDTO);

    UsersDTO toDTO(UsersEntity usersEntity);
}
