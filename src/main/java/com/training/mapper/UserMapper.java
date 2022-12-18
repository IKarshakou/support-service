package com.training.mapper;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.entity.enums.Role;
import com.training.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = Role.class)//(nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    OutputUserDto convertToDto(User user);

    @Mapping(target = "role", defaultExpression = "java(Role.EMPLOYEE)")
    User convertToEntity(InputUserDto inputUserDto);
//    default User convertToEntity(InputUserDto inputUserDto) {
//        return User.builder()
//                .email(inputUserDto.getEmail())
//                .password(inputUserDto.getPassword())
//                .role(Role.EMPLOYEE)
//                .build();
//    }

    User convertUpdatedToEntity(UpdatedUserDto updatedUserDto);

//    List<UserDto> convertListToDto(List<User> users);
//
//    List<User> convertListToEntity(List<UserDto> usersDto);
}
