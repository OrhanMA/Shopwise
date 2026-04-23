package com.shopwise.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "services")
public class ServiceOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;
    @Column(name = "points_reward", nullable = false)
    private int pointsReward;
    private boolean active;

    public Long getId() { return id; }
    public Shop getShop() { return shop; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getPointsReward() { return pointsReward; }
    public boolean isActive() { return active; }
}
