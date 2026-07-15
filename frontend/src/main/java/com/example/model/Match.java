package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}),
        indexes = {
                @Index(name = "idx_match_user1", columnList = "user1_id"),
                @Index(name = "idx_match_user2", columnList = "user2_id")
        }
)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Always store smaller ID first (important!)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Match() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // GETTERS / SETTERS

    public Long getId() { return id; }

    public User getUser1() { return user1; }

    public User getUser2() { return user2; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setUser1(User user1) { this.user1 = user1; }

    public void setUser2(User user2) { this.user2 = user2; }

    public void setUsers(User u1, User u2) {
        if (u1.getId() < u2.getId()) {
            this.user1 = u1;
            this.user2 = u2;
        } else {
            this.user1 = u2;
            this.user2 = u1;
        }
    }
}