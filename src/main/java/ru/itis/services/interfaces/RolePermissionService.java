package ru.itis.services.interfaces;
import ru.itis.entities.RolePermission;
import java.util.List;

public interface RolePermissionService {
    void create(RolePermission rolePermission);
    RolePermission getById(Long roleId, Long permissionId);
    List<RolePermission> getAll();
    void update(Long roleId, Long permissionId, RolePermission updated);
    void delete(Long roleId, Long permissionId);
}
