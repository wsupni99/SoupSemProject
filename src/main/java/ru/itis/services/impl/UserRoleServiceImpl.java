package ru.itis.services.impl;

import ru.itis.entities.Role;
import ru.itis.entities.UserRole;
import ru.itis.entities.UserRoleId;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.RoleRepository;
import ru.itis.repositories.interfaces.UserRoleRepository;
import ru.itis.services.interfaces.UserRoleService;

import java.util.List;

public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void create(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    public UserRole getById(Long userId, Long roleId) {
        UserRoleId id = new UserRoleId(userId, roleId);
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserRole not found"));
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public void update(UserRole updated) {
        userRoleRepository.update(updated);
    }

    @Override
    public void delete(Long userId, Long roleId) {
        UserRoleId id = new UserRoleId(userId, roleId);
        userRoleRepository.deleteById(id);
    }

    @Override
    public boolean isAdmin(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        for (UserRole ur : userRoles) {
            Role role = roleRepository.findById(ur.getRoleId()).orElse(null);
            if (role != null && "admin".equalsIgnoreCase(role.getRoleName())) return true;
        }
        return false;
    }
}