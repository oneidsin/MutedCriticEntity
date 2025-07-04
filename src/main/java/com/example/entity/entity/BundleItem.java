package com.example.entity.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "bundle_item")
public class BundleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_idx")
    private int BundleIdx;

    @Column(name = "bundle_price", length = 6)
    private int bundlePrice;

    @Column(name = "bundle_name", length = 100)
    private String bundleName;

    @Column(name = "sell_type", length = 50)
    private String sellType;

    @Column(name = "sell_start_date")
    private LocalDate sellStartDate;

    @Column(name = "sell_end_date")
    private LocalDate sellEndDate;

}
