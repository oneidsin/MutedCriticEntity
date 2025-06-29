package com.example.entity.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPw;

    @Column(name = "user_nick")
    private String userNick;

    @Column(name = "user_gender")
    private String userGender;

    @Column(name = "user_level")
    private int userLevel;

    @Column(name = "region")
    private String region;

    @CreationTimestamp
    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "withdraw_date")
    private LocalDateTime withdrawDate;

    @Column(name = "receive_yn")
    private Boolean receiveYn;
}
