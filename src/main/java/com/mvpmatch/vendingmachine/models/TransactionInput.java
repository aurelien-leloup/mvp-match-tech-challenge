package com.mvpmatch.vendingmachine.models;

import lombok.Data;

import java.util.List;

@Data
public class TransactionInput {
    private List<ProductPurchase> productPurchases;
}
