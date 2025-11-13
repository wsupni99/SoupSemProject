package ru.itis.services.interfaces;

import ru.itis.entities.Permission;
import java.util.List;

public interface PermissionService {
    void create(Permission permission);
    Permission getById(Long id);
    List<Permission> getAll();
    void update(Permission updated);
    void delete(Long id);
    boolean hasPermission(Long userId, String permissionName);
    List<Permission> getUserPermissions(Long userId);
    boolean roleHasPermission(Long roleId, String permissionName);
}