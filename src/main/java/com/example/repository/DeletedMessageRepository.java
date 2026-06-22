package com.example.repository;

import com.example.model.DeletedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeletedMessageRepository
        extends JpaRepository<DeletedMessage,Long> {

    boolean existsByUserIdAndMessageId(
            Long userId,
            Long messageId
    );

    List<DeletedMessage> findByUserId(
            Long userId
    );
}