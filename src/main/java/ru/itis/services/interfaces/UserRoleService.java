package ru.itis.services.interfaces;

import ru.itis.entities.UserRole;

import java.util.List;

public interface UserRoleService {
    void create(UserRole userRole);
    UserRole getById(Long userId, Long roleId);
    List<UserRole> getAll();
    void update(UserRole updated);
    void delete(Long userId, Long roleId);
    boolean isAdmin(Long userId);

    boolean isManager(Long userId);

    boolean isDeveloper(Long userId);

    boolean isTester(Long userId);

    void assignRole(Long userId, Long roleId);

    List<UserRole> findByUserId(Long userId);
}
