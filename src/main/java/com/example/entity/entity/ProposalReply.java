package com.example.entity.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "proposal_reply")
public class ProposalReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_idx")
    private int replyIdx;

    @OneToOne
    @JoinColumn(name = "proposal_idx")
    private Proposal proposal;

    @Column(name = "sender_email", length = 50)
    private String senderEmail;

    @Column(name = "reply_subject", length = 1000)
    private String replySubject;

    @Lob
    @Column(name = "reply_content", columnDefinition = "longtext")
    private String replyContent;

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    @Column(name = "is_important")
    private boolean isImportant;



}
