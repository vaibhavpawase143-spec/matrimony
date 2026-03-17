package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_photos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "photoType"})
        })
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String photoType; // PROFILE, KUNDALI

    private String photoUrl; // file path

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {    // Added setter for id
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}