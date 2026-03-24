package com.example.serviceimpl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    // ✅ Register
    @Override
    public User register(User user) {

        if (repository.existsByEmailIgnoreCaseOrPhone(user.getEmail(), user.getPhone())) {
            throw new RuntimeException("Email or phone already exists!");
        }

        user.setIsActive(true);

        // 👉 IMPORTANT: In real app, password should be encrypted
        return repository.save(user);
    }

    // 🔐 Login
    @Override
    public User login(String email, String password) {

        User user = repository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or user inactive"));

        // 👉 Plain password check (for now)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // 🔍 Get by ID
    @Override
    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get by email
    @Override
    public Optional<User> getByEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    // 🔍 Get all
    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    // 🔍 Active users
    @Override
    public List<User> getActiveUsers() {
        return repository.findByIsActiveTrue();
    }

    // 🔍 Search
    @Override
    public List<User> search(String keyword) {
        return repository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );
    }

    // ✅ Update
    @Override
    public User update(Long id, User user) {

        User existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Optional duplicate check (if email/phone changed)
        if (!existing.getEmail().equalsIgnoreCase(user.getEmail()) ||
                !existing.getPhone().equals(user.getPhone())) {

            if (repository.existsByEmailIgnoreCaseOrPhone(user.getEmail(), user.getPhone())) {
                throw new RuntimeException("Email or phone already exists!");
            }
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setPassword(user.getPassword());

        return repository.save(existing);
    }

    // ❌ Soft delete
    @Override
    public void deactivate(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setIsActive(false);
        repository.save(user);
    }
}