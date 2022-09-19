package com.mvpmatch.vendingmachine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Change {
    private int coinValue;
    private int quantity;
}
