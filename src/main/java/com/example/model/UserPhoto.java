package com.example.model;

import com.example.model.base.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_photos",
        indexes = {
                @Index(
                        name = "idx_user_photo_user",
                        columnList = "user_id"
                )
        }
)
public class UserPhoto extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // OWNER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // =====================================================
    // PHOTO TYPE
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "photo_type", nullable = false, length = 50)
    private PhotoType photoType;

    // =====================================================
    // PHOTO URL
    // =====================================================

    @Column(name = "photo_url", nullable = false, length = 1000)
    private String photoUrl;

    // =====================================================
    // PRIMARY PHOTO
    // =====================================================

    @Builder.Default
    @Column(name = "is_primary", nullable = false)
    private Boolean primaryPhoto = false;
}