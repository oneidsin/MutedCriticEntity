package com.example.entity.dummy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.AccessStats;

public interface AccessStatsRepository extends JpaRepository<AccessStats, Integer> {

}
