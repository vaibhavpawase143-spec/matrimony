package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="diets")
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String name;



    private Boolean status = true;
}