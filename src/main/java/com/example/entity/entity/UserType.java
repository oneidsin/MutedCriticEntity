package com.example.entity.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user_type")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_idx")
    private int typeIdx;

    @Column(name = "type_name")
    private String typeName;

    // 외래키(user 테이블의 user_id)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
