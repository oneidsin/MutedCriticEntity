package com.example.entity.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stats_activity_daily")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsActivityDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "dau")
    private int dau;

    @Column(name = "new_user_count")
    private int newUserCount;

    @Column(name = "dormant_user_count")
    private int dormantUserCount;

    @Column(name = "withdrawn_user_count")
    private int withdrawnUserCount;
}