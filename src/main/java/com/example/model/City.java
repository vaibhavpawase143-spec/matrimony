package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "cities",
        indexes = {
                @Index(name = "idx_city_name", columnList = "name"),
                @Index(name = "idx_city_state", columnList = "state_id"),
                @Index(name = "idx_city_active", columnList = "is_active")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_city_state_name",
                        columnNames = {"state_id", "name"}
                )
        }
)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}