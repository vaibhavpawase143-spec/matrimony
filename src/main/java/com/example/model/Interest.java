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
        name = "interests",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"sender_id", "receiver_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_interest_sender",
                        columnList = "sender_id"
                ),
                @Index(
                        name = "idx_interest_receiver",
                        columnList = "receiver_id"
                )
        }
)
public class Interest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // SENDER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // =====================================================
    // RECEIVER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // =====================================================
    // STATUS
    // =====================================================

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    // =====================================================
    // ACTIVE
    // =====================================================

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}