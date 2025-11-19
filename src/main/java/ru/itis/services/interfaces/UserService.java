package ru.itis.services.interfaces;

import ru.itis.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void deleteUser(Long id);

    Optional<User> getByEmail(String login);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    List<User> getAllManagers();

    void register(String email, String name, String rawPassword);
    Optional<User> login(String email, String rawPassword);

    // Если успею для админки
    void updateUser(User user);
}
