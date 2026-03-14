package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="complexions")
public class Complexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    private String value;



    private Boolean status = true;
}