package com.example.entity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailFileId implements Serializable {

    @Column(name = "mail_idx")
    private int mailIdx;

    @Column(name = "file_idx")
    private int fileIdx;
}