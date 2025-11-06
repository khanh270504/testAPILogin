package com.hello.identy_sevice.dto.request;

import com.hello.identy_sevice.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
   String name;
   String description;
   Set<String> permissions;
}
