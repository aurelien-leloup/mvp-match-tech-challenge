package com.mvpmatch.vendingmachine.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@Entity
public class Product {

    @Column
    private int amountAvailable;

    @Column
    private int cost;

    @Column
    @Id
    private String productName;

    @Column
    private String sellerId;
}
