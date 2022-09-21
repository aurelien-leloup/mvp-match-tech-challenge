package com.mvpmatch.vendingmachine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPurchase {
    private String productId;
    private int quantity;
}
