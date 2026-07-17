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
        name = "conversations",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user1_id", "user2_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_conversation_user1",
                        columnList = "user1_id"
                ),
                @Index(
                        name = "idx_conversation_user2",
                        columnList = "user2_id"
                )
        }
)
public class Conversation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // PARTICIPANT 1
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    // =====================================================
    // PARTICIPANT 2
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
}