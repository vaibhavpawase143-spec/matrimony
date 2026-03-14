package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sub_castes")
public class SubCaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String name;

    @ManyToOne
    @JoinColumn(name="caste_id")
    private Caste caste;



    private Boolean status = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}