package com.example.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.RefundList;

public interface RefundRepository extends JpaRepository<RefundList, Integer> {

}
