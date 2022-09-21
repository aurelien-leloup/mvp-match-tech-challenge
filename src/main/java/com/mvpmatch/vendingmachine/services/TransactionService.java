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
            int coinValue = findCoinValue(changeTotal);
            int coinNumber = changeTotal / coinValue;
            change.add(new Change(coinValue, coinNumber));
            changeTotal -= coinNumber * coinValue;
        }

        result.setChange(change);
        user.setDeposit(0);
        userService.update(user);
        return result;
    }


    private int findCoinValue(int changeTotal) {
        for (int coin : List.of(100, 50, 20, 10)) {
            if (changeTotal >= coin) {
                return coin;
            }
        }
        return 5;
    }
}
