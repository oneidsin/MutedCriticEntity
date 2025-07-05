package com.example.entity.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stats_revenue_daily")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsRevenueDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "daily_revenue", precision = 19, scale = 2)
    private BigDecimal dailyRevenue;

    @Column(name = "daily_purchase_count")
    private int dailyPurchaseCount;

    @Column(name = "daily_paying_users")
    private int dailyPayingUsers;

    @Column(name = "arppu", precision = 19, scale = 2)
    private BigDecimal arppu;

    @Column(name = "daily_interval")
    private int dailyInterval;
}