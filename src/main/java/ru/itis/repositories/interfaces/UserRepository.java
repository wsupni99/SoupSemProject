package ru.itis.repositories.interfaces;

import ru.itis.entities.User;
import ru.itis.util.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String passwordHash);
    List<User> findByRole(Long roleId);
}
