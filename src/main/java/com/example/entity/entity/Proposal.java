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
@Table(name = "proposal")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_idx")
    private int proposalIdx;

    // 외래키(influencers 테이블의 inf_id)
    @ManyToOne
    @JoinColumn(name = "inf_id")
    private Influencers influencers;

    // 외래키(member 테이블의 member_id)
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "proposal_sub", length = 1000)
    private String proposalSub;

    @Lob
    @Column(name = "proposal_content", columnDefinition = "longtext")
    private String proposalContent;

    @Column(name = "proposal_accept")
    private boolean proposalAccept;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

}
