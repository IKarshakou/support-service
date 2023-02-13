package com.training.mapper;

import com.training.dto.role.OutputRoleDto;
import com.training.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface RoleMapper {
    @Mapping(target = "permissions", expression = "java(permissionsToStringSet(role))")
    OutputRoleDto convertToDto(Role role);

    default Set<String> permissionsToStringSet(Role role) {
        return role.getPermissions().stream()
                .map(permission -> permission.getName().name())
                .collect(Collectors.toSet());
    }
}
