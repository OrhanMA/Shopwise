package com.shopwise.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_transactions")
public class LoyaltyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private String reason;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private LoyaltyTransactionType transactionType;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public LoyaltyTransaction() {}

    public LoyaltyTransaction(Shop shop, Customer customer, Appointment appointment, int points, String reason, LoyaltyTransactionType transactionType) {
        this.shop = shop;
        this.customer = customer;
        this.appointment = appointment;
        this.points = points;
        this.reason = reason;
        this.transactionType = transactionType;
    }

    public Long getId() { return id; }
    public int getPoints() { return points; }
    public String getReason() { return reason; }
    public LoyaltyTransactionType getTransactionType() { return transactionType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
