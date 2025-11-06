package com.hello.identy_sevice.repository;

import com.hello.identy_sevice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
