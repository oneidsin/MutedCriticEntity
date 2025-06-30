package com.example.entity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "influencers")
public class Influencers {

    @Id
    @Column(name = "inf_id", length = 500)
    private String infId;

    @Column(name = "inf_contact", length = 100)
    private String infContact;

    @Column(name = "inf_platform", length = 100)
    private String infPlatform;

    @Column(name = "inf_level")
    private int infLevel;

}
