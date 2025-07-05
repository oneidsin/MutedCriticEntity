package com.example.entity.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stats_item_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "item_idx" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsItemDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "item_idx")
    private int itemIdx;

    @Column(name = "daily_sales_revenue", precision = 19, scale = 2)
    private BigDecimal dailySalesRevenue;

    @Column(name = "daily_sales_count")
    private int dailySalesCount;

    @Column(name = "daily_paying_users")
    private int dailyPayingUsers;

    @Column(name = "daily_refund_amount", precision = 19, scale = 2)
    private BigDecimal dailyRefundAmount;

    @Column(name = "daily_refund_count")
    private int dailyRefundCount;

    @Column(name = "returning_user_buy_count")
    private int returningUserBuyCount;

    @Column(name = "regular_user_buy_count")
    private int regularUserBuyCount;

    @Column(name = "vip_user_buy_count")
    private int vipUserBuyCount;
}