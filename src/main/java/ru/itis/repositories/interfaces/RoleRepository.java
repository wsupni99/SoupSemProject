package ru.itis.repositories.interfaces;

import ru.itis.entities.Role;
import ru.itis.util.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    void save(Role role);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    void update(Role role);
    void deleteById(Long id);
    Optional<Role> findByName(String name);
}
