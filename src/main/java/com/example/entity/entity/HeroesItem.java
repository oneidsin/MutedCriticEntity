package com.example.entity.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

// 영웅이 보유한 아이템
@Entity
@Data
@Table(name = "heroes_item")
public class HeroesItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private ItemList itemList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heroes_idx")
    private Heroes heroes;

}
