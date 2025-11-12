package ru.itis.services.interfaces;

import ru.itis.entities.Role;
import java.util.List;

public interface RoleService {
    void create(Role role);
    Role getById(Long id);
    List<Role> getAll();
    void update(Long id, Role updated);
    void delete(Long id);
}
