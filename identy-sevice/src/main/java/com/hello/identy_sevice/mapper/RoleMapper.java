package com.hello.identy_sevice.mapper;

import com.hello.identy_sevice.dto.request.RoleRequest;
import com.hello.identy_sevice.dto.response.RoleResponse;
import com.hello.identy_sevice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
