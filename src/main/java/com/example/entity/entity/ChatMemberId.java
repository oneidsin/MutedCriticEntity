package com.example.entity.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberId implements Serializable {

    @Column(name = "room_idx")
    private int roomIdx;

    @Column(name = "member_id", length = 50)
    private String memberId;

}