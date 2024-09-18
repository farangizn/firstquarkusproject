package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entity.User;
import org.example.interfaces.UserService;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.persist(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.listAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    @Override
    public void update(User existingUser, User updatedUser) {
        existingUser.setName(updatedUser.getName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        userRepository.persist(existingUser);
    }
}
