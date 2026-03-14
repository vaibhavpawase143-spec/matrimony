package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "castes")
public class Caste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="religion_id")
    private Religion religion;

    private Boolean status = true;

}