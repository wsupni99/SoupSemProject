package ru.itis.services.interfaces;

import ru.itis.entities.Permission;
import java.util.List;

public interface PermissionService {
    void create(Permission permission);
    Permission getById(Long id);
    List<Permission> getAll();
    void update(Long id, Permission updated);
    void delete(Long id);

    // Функционал для проверки доступа
    boolean hasPermission(Long userId, String permissionName);
    List<Permission> getUserPermissions(Long userId);
    boolean roleHasPermission(Long roleId, String permissionName);
}