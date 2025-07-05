package com.example.entity.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "mail")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_idx")
    private int mailIdx;

    // 메일 템플릿 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tem_idx")
    private MailTemplate mailTemplate;

    // 회원 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @Column(name = "is_to_all")
    private boolean isToAll;

    @Column(name = "recipient", columnDefinition = "text")
    private String recipient;

    @Column(name = "mail_sub")
    private String mailSub;

    @Lob
    @Column(name = "mail_content", columnDefinition = "longtext")
    private String mailContent;

    @Column(name = "mail_date")
    private LocalDateTime mailDate;

}
