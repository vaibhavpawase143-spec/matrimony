package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="family_status")
public class FamilyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;

    private Boolean status = true;
}