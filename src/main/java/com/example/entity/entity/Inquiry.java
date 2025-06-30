package com.example.entity.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_idx")
    private int inquiryIdx;

    // 외래키(user 테이블의 user_id)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 외래키(user 테이블의 user_id)
    @ManyToOne
    @JoinColumn(name = "reported_id")
    private User reportedUser;

    @Column(name = "category", length = 50)
    private String category;

    @Lob
    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
