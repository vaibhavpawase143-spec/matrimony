package com.example.model;

import com.example.model.User;
import jakarta.persistence.*;

@Entity
@Table(name="payments")
public class Payment {

    @Id
    @
            GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Double amount;

    private String paymentMethod;

    private String transactionId;

    private String paymentStatus;
}