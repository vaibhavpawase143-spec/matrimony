package com.example.model;

import com.example.model.base.Auditable;
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
        name = "shortlists",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "profile_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_shortlist_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_shortlist_profile",
                        columnList = "profile_id"
                )
        }
)
public class Shortlist extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // USER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =====================================================
    // PROFILE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // =====================================================
    // STATUS
    // =====================================================

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}