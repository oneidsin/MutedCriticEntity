package com.example.entity.dummy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.BundleItem;

public interface BundleRepository extends JpaRepository<BundleItem, Integer> {

}
