package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="heights")
public class Height {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String height;

    private Boolean status = true;

}