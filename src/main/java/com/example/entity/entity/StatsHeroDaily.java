package com.example.entity.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stats_hero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "heroes_idx",
        "tier_name" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsHeroDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hero_stats_id", precision = 19, scale = 0)
    private Long heroStatsId;

    @Column(name = "heroes_idx")
    private int heroesIdx;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "tier_name", length = 20)
    private String tierName;

    @Column(name = "win_count")
    private int winCount;

    @Column(name = "lose_count")
    private int loseCount;

    @Column(name = "pick_count")
    private int pickCount;

    @Column(name = "ban_count")
    private int banCount;

    @Column(name = "pota_count")
    private int potaCount;

    @Column(name = "total_play_hours")
    private int totalPlayHours;
}