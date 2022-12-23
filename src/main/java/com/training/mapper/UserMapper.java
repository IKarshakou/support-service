package com.training.mapper;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.entity.enums.Role;
import com.training.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = Role.class)
public interface UserMapper {

    OutputUserDto convertToDto(User user);

    @Mapping(target = "role", expression = "java(getEmployeeRole())")
    User convertToEntity(InputUserDto inputUserDto);

    List<OutputUserDto> convertListToDto(List<User> users);

    default Role getEmployeeRole() {
        return Role.EMPLOYEE;
    }
}
