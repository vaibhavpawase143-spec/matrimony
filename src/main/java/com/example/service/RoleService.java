package com.example.service;

import com.example.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Role create(Role role);

    List<Role> getAll();

    Optional<Role> getById(Long id);

    Role update(Long id, Role role);

    void delete(Long id); // soft delete

    List<Role> search(String keyword);

    List<Role> getByAdmin(Long adminId);
}