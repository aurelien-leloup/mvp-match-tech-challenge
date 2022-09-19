package com.mvpmatch.vendingmachine.controllers;

import com.mvpmatch.vendingmachine.models.Deposit;
import com.mvpmatch.vendingmachine.models.TransactionInput;
import com.mvpmatch.vendingmachine.models.TransactionResult;
import com.mvpmatch.vendingmachine.services.TransactionService;
import com.mvpmatch.vendingmachine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_BUYER')")
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @PutMapping("/deposit")
    public void deposit(@RequestBody Deposit deposit, Authentication authentication) {
        this.userService.setDeposit(deposit.getValue(), authentication);
    }

    @PostMapping("/buy")
    @ResponseBody
    public TransactionResult buy(@RequestBody TransactionInput transactionInput, Authentication authentication) {
        return transactionService.buy(transactionInput, authentication);
    }

    @PostMapping("/reset")
    public void reset(Authentication authentication) {
        this.userService.reset(authentication);
    }
}
