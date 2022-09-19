package com.mvpmatch.vendingmachine.models;

import lombok.Data;

import java.util.List;

@Data
public class TransactionResult {
    private int totalSpent;
    private List<ProductPurchase> productsPurchased;
    private List <Change> change;

}
