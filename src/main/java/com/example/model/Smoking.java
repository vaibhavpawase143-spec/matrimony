package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="smoking")
public class Smoking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin admin;

    private Boolean status = true;
}