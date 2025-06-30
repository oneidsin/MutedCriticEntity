package com.example.entity.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id", length = 50)
    private String memberId;

    @Column(name = "member_pw", length = 50)
    private String memberPw;

    @Column(name = "member_nick", length = 50)
    private String memberNick;

    @Column(name = "member_gender", length = 10)
    private String memberGender;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "withdraw_date")
    private LocalDate withdrawDate;

    @Column(name = "job", length = 20)
    private String job;

    @Column(name = "dept_name", length = 20)
    private String deptName;

}
