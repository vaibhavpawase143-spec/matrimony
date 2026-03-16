package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partner_preferences")
public class PartnerPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private Integer minAge;
    private Integer maxAge;

    @ManyToOne
    @JoinColumn(name="religion_id")
    private Religion religion;

    @ManyToOne
    @JoinColumn(name="caste_id")
    private Caste caste;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

}