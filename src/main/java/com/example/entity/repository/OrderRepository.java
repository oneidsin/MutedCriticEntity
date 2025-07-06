package com.example.entity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.OrderList;

public interface OrderRepository extends JpaRepository<OrderList, Integer> {

    List<OrderList> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

}
