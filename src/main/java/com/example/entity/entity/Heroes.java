package com.example.entity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "heroes")
public class Heroes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heroes_idx")
    private int heroesIdx;

    @Column(name = "heroes_name", length = 50)
    private String heroesName;

    @Column(name = "role", length = 10)
    private String role;
    
}
