package ru.itis.services.impl;
import ru.itis.entities.RolePermission;
import java.util.List;

import ru.itis.entities.RolePermissionId;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.RolePermissionRepository;
import ru.itis.services.interfaces.RolePermissionService;

public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public void create(RolePermission rolePermission) {
        rolePermissionRepository.save(rolePermission);
    }

    @Override
    public RolePermission getById(Long roleId, Long permissionId) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        return rolePermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found"));
    }

    @Override
    public List<RolePermission> getAll() {
        return rolePermissionRepository.findAll();
    }

    @Override
    public void update(RolePermission updated) {
        RolePermission existing = getById(updated.getRoleId(), updated.getPermissionId());
        existing.setRoleId(updated.getRoleId());
        existing.setPermissionId(updated.getPermissionId());
        rolePermissionRepository.save(existing);
    }

    @Override
    public void delete(Long roleId, Long permissionId) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        rolePermissionRepository.deleteById(id);
    }
}