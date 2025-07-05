package com.example.entity.dummy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
