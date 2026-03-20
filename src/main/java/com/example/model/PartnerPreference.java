package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "partner_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"})
)
public class PartnerPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer minAge;
    private Integer maxAge;

    private Double minHeight;
    private Double maxHeight;

    @ManyToOne
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @ManyToOne
    @JoinColumn(name = "caste_id")
    private Caste caste;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public Double getMinHeight() { return minHeight; }
    public void setMinHeight(Double minHeight) { this.minHeight = minHeight; }

    public Double getMaxHeight() { return maxHeight; }
    public void setMaxHeight(Double maxHeight) { this.maxHeight = maxHeight; }

    public Religion getReligion() { return religion; }
    public void setReligion(Religion religion) { this.religion = religion; }

    public Caste getCaste() { return caste; }
    public void setCaste(Caste caste) { this.caste = caste; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
}