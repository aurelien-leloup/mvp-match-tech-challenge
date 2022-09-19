package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    public TransactionResult buy(TransactionInput input, Authentication authentication) {
        TransactionResult result = new TransactionResult();

        final String username = this.authService.getUsernameFromAuth(authentication);
        User user = this.userService.read(username);

        int purchaseTotal = 0;

        List<Product> updatedProducts = new ArrayList<>();

        for (ProductPurchase productPurchase : input.getProductPurchases()) {
            Product product = productService.read(productPurchase.getProductId());

            if (product.getAmountAvailable() < productPurchase.getQuantity()) {
                throw new InvalidInputException("Product " + productPurchase.getProductId() + " stock is too low.");
            }

            product.setAmountAvailable(product.getAmountAvailable() - productPurchase.getQuantity());
            updatedProducts.add(product);
            purchaseTotal += product.getCost() * productPurchase.getQuantity();
        }

        if (user.getDeposit() < purchaseTotal) {
            throw new InvalidInputException("User deposit is too low");
        }


        for (Product product : updatedProducts) {
            productService.save(product);
        }

        result.setTotalSpent(purchaseTotal);
        result.setProductsPurchased(input.getProductPurchases());

        int changeTotal = user.getDeposit() - purchaseTotal;
        List<Change> change = new ArrayList<>();

        while (changeTotal > 0) {
            int coinNumber;
            if (changeTotal >= 100) {
                coinNumber = changeTotal / 100;
                change.add(new Change(100, coinNumber));
                changeTotal -= coinNumber * 100;
            } else if (changeTotal >= 50) {
                coinNumber = changeTotal / 50;
                change.add(new Change(50, coinNumber));
                changeTotal -= coinNumber * 50;
            } else if (changeTotal >= 20) {
                coinNumber = changeTotal / 20;
                change.add(new Change(20, coinNumber));
                changeTotal -= coinNumber * 20;
            } else if (changeTotal >= 10) {
                coinNumber = changeTotal / 10;
                change.add(new Change(10, coinNumber));
                changeTotal -= coinNumber * 10;
            } else {
                coinNumber = changeTotal / 5;
                change.add(new Change(5, coinNumber));
                changeTotal -= coinNumber * 5;
            }
        }

        result.setChange(change);
        user.setDeposit(0);
        userService.save(user);
        return result;
    }
}
