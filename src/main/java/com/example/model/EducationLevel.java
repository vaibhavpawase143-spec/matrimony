package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "education_levels",
        indexes = {
                @Index(name = "idx_education_level_name", columnList = "name")
        }
)
public class EducationLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Boolean status = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}