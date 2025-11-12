package ru.itis.services.impl;

import ru.itis.entities.Permission;
import ru.itis.entities.RolePermission;
import ru.itis.entities.UserRole;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.PermissionRepository;
import ru.itis.repositories.interfaces.RolePermissionRepository;
import ru.itis.repositories.interfaces.UserRoleRepository;
import ru.itis.services.interfaces.PermissionService;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository,
                                 UserRoleRepository userRoleRepository,
                                 RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public void create(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public Permission getById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found"));
    }

    @Override
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    @Override
    public void update(Long id, Permission updated) {
        Permission existing = getById(id);
        existing.setPermissionName(updated.getPermissionName());
        existing.setDescription(updated.getDescription());
        permissionRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    // Функционал для проверки доступа
    @Override
    public boolean hasPermission(Long userId, String permissionName) {
        List<UserRole> roles = userRoleRepository.findByUserId(userId);
        for (UserRole ur : roles) {
            if (roleHasPermission(ur.getRoleId(), permissionName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Permission> getUserPermissions(Long userId) {
        List<UserRole> roles = userRoleRepository.findByUserId(userId);
        return roles.stream()
                .flatMap(r -> rolePermissionRepository.findByRoleId(r.getRoleId()).stream())
                .map(rp -> permissionRepository.findById(rp.getPermissionId())
                        .orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean roleHasPermission(Long roleId, String permissionName) {
        List<RolePermission> perms = rolePermissionRepository.findByRoleId(roleId);
        for (RolePermission rp : perms) {
            Permission perm = permissionRepository.findById(rp.getPermissionId()).orElse(null);
            if (perm != null && perm.getPermissionName().equals(permissionName)) {
                return true;
            }
        }
        return false;
    }
}
