package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile_visitors")
public class ProfileVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    @JsonIgnoreProperties({
            "roles",
            "profile",
            "partnerPreference"
    })
    private User visitor;

    @ManyToOne
    @JoinColumn(name = "visited_user_id")
    @JsonIgnoreProperties({
            "roles",
            "profile",
            "partnerPreference"
    })
    private User visitedUser;

    private LocalDateTime viewedAt;

    @PrePersist
    public void onCreate() {

        viewedAt = LocalDateTime.now();

    }
}