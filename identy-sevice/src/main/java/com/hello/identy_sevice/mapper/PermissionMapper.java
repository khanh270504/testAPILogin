package com.hello.identy_sevice.mapper;

import com.hello.identy_sevice.dto.request.PermissionRequest;
import com.hello.identy_sevice.dto.response.PermissionResponse;
import com.hello.identy_sevice.entity.Permission;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
