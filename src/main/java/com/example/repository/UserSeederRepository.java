package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSeederRepository extends JpaRepository<com.example.model.User, Long> {

    @Query(value = "SELECT nextval('users_id_seq')", nativeQuery = true)
    Long nextUserId();

}