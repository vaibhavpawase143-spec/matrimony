package com.example.serviceimpl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    // ✅ Register
    @Override
    public User register(User user) {

        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }

        // 🔥 No need to set timestamps (handled automatically)
        user.setActive(true);

        return repo.save(user);
    }

    // ✅ Login
    @Override
    public Optional<User> login(String email, String password) {
        return repo.findByEmailAndPasswordAndActiveTrue(email, password);
    }

    // ✅ Get by ID
    @Override
    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get all
    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active users
    @Override
    public List<User> getAllActive() {
        return repo.findByActiveTrue();
    }

    // ✅ Update
    @Override
    public User update(Long id, User user) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setPassword(user.getPassword());

        return repo.save(existing);
    }

    // ✅ Deactivate (Soft Delete)
    @Override
    public void deactivate(Long id) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existing.setActive(false);

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<User> search(String keyword) {
        return repo.findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    // ✅ Check email exists
    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }
}