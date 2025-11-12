package ru.itis.repositories.interfaces;

import ru.itis.entities.RolePermission;
import ru.itis.entities.RolePermissionId;
import ru.itis.util.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends CrudRepository<RolePermission, RolePermissionId> {
    Optional<RolePermission> findById(RolePermissionId id);
    List<RolePermission> findByRoleId(Long roleId);
}
