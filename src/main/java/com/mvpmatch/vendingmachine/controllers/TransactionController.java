package com.mvpmatch.vendingmachine.controllers;

import com.mvpmatch.vendingmachine.models.Amount;
import com.mvpmatch.vendingmachine.models.TransactionResult;
import com.mvpmatch.vendingmachine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    UserService userService;

    @PutMapping("/deposit")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public void deposit(@RequestBody Amount amount, Authentication authentication) {
        this.userService.setDeposit(amount.getAmount(), authentication);
    }

    @PostMapping("/buy")
    @ResponseBody
    public TransactionResult buy() {
        return null;
    }

    @PostMapping("/reset")
    public void reset() {
    }
}
