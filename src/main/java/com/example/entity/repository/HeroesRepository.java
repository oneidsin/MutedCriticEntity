package com.example.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.Heroes;

public interface HeroesRepository extends JpaRepository<Heroes, Integer> {

}
