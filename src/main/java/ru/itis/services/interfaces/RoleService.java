package ru.itis.services.interfaces;

import ru.itis.entities.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    void create(Role role);
    void update(Role updated);
    void delete(Long id);

    Role getById(Long id);
    Optional<Long> getRoleIdByName(String name);
    List<Role> getAll();
}
