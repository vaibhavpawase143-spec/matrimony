package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="profile_types")
public class ProfileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String name;

}