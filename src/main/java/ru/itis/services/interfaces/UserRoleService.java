package ru.itis.services.interfaces;

import ru.itis.entities.UserRole;

import java.util.List;

public interface UserRoleService {
    void create(UserRole userRole);
    void update(UserRole updated);
    void delete(Long userId, Long roleId);

    UserRole getById(Long userId, Long roleId);
    List<UserRole> getAll();
    List<UserRole> findByUserId(Long userId);

    boolean isAdmin(Long userId);
    boolean isManager(Long userId);
    boolean isDeveloper(Long userId);
    boolean isTester(Long userId);

    boolean userHasRole(Long userId, String roleName);

    // Если успею для админки
    void assignRole(Long userId, Long roleId);
}
