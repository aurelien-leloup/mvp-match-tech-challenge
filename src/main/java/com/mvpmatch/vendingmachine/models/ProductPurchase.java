package com.mvpmatch.vendingmachine.models;

import lombok.Data;

@Data
public class ProductPurchase {
    private String productId;
    private int quantity;
}
