package com.example.entity.dummy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Integer> {

}
