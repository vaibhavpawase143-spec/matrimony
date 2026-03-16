package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="user_photos")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String photoType; // PROFILE, KUNDALI

    private String photoUrl; // file path

}