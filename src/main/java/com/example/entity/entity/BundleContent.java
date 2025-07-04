package com.example.entity.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "bundle_content")
public class BundleContent {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_idx")
    private BundleItem bundleItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private ItemList itemList;

}
