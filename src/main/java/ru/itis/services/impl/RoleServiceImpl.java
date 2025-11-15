package ru.itis.services.impl;

import ru.itis.entities.Role;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.RoleRepository;
import ru.itis.services.interfaces.RoleService;

import java.util.List;
import java.util.Optional;

public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void create(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public void update(Role updated) {
        Role existing = getById(updated.getRoleId());
        existing.setRoleName(updated.getRoleName());
        existing.setDescription(updated.getDescription());
        roleRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Long> getRoleIdByName(String name) {
        return roleRepository.findByName(name).map(Role::getRoleId);
    }

}
