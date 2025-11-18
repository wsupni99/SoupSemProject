package ru.itis.services.interfaces;

import ru.itis.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByEmail(String login);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
    void createUser(User user);
    List<User> getAllManagers();
    void register(String email, String name, String rawPassword);
    Optional<User> login(String email, String rawPassword);
}
