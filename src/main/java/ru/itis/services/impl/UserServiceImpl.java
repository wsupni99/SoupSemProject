package ru.itis.services.impl;

import ru.itis.entities.User;
import ru.itis.repositories.interfaces.UserRepository;
import ru.itis.services.interfaces.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    @Override
    public Optional<User> findByEmailAndPassword(String email, String hashedPassword) {
        return userRepository.findByEmailAndPassword(email, hashedPassword);
    }
}
