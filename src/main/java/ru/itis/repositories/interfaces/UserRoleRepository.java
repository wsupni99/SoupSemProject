package ru.itis.repositories.interfaces;

import ru.itis.entities.UserRole;
import ru.itis.entities.UserRoleId;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserId(Long userId);
    List<UserRole> findByRoleId(Long roleId);
}

