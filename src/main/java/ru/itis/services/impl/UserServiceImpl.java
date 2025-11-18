package ru.itis.services.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.entities.User;
import ru.itis.repositories.interfaces.UserRepository;
import ru.itis.services.interfaces.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Хеширование через Spring Security
    @Override
    public void register(String email, String name, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setContactInfo("");
        userRepository.save(user);
    }

    @Override
    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email.trim());
        if (!userOpt.isPresent()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(rawPassword.trim(), user.getPasswordHash())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }
    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public Optional<User> getByEmail(String login){
        return userRepository.findByEmail(login);
    }
    @Override
    public List<User> getAllManagers() {
        return userRepository.findByRole(2L);
    }
}
