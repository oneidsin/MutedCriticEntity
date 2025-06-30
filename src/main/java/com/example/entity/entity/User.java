package com.example.entity.entity;

import java.time.LocalDate;
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
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "user_pw", length = 50)
    private String userPw;

    @Column(name = "user_nick", length = 50)
    private String userNick;

    @Column(name = "user_gender", length = 10)
    private String userGender;

    @Column(name = "user_level")
    private int userLevel;

    @Column(name = "region", length = 20)
    private String region;

    @CreationTimestamp
    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "withdraw_date")
    private LocalDate withdrawDate;

    @Column(name = "last_access_date")
    private LocalDateTime lastAccessDate;

    @Column(name = "user_new")
    private boolean userNew;

    @Column(name = "user_return")
    private boolean userReturn;

    @Column(name = "user_normal")
    private boolean userNormal;

    @Column(name = "user_vip")
    private boolean userVip;

    @Column(name = "user_dormant")
    private boolean userDormant;

    @Column(name = "receive_yn")
    private boolean receiveYn;

    @Column(name = "user_suspend")
    private boolean userSuspend;

    @Column(name = "churn_risk")
    private boolean churnRisk;

    @Column(name = "total_play_time")
    private int totalPlayTime;
}
