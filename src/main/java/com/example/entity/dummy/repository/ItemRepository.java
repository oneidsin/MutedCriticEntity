package com.example.entity.dummy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.ItemList;

public interface ItemRepository extends JpaRepository<ItemList, Integer> {

}
