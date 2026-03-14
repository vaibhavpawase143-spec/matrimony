package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="body_types")
public class BodyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String value;


    private Boolean status = true;
}