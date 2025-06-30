package com.example.entity.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "order_list")
public class OrderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_idx")
    private int orderIdx;

    // 외래키(user 테이블의 user_id)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 외래키(item_list 테이블의 item_idx)
    @ManyToOne
    @JoinColumn(name = "item_idx")
    private ItemList itemList;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "sales")
    private int sales;

}
