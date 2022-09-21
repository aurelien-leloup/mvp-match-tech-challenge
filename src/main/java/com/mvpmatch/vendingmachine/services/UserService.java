package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.exceptions.NotFoundException;
import com.mvpmatch.vendingmachine.models.Deposit;
import com.mvpmatch.vendingmachine.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserRepository repository;

    @Autowired
    AuthService authService;

    public User create(User user) {
        try {
            read(user.getUsername());
        } catch (NotFoundException e) {
            user.setPassword(encoder.encode(user.getPassword()));
            return this.repository.save(user);
        }
        throw new InvalidInputException("User already exists");
    }

    public User read(String username) {
        return this.repository
                .findById(username)
                .orElseThrow(() ->new NotFoundException("User", username));
    }

    public User update(User user) {
        read(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    public void delete(String username) {
        User user = read(username);
        this.repository.delete(user);
    }

    public void setDeposit(Integer amount, Authentication authentication) {
        List <Integer> coins = List.of(5, 10, 20, 50, 100);
        if (!coins.contains(amount)) {
            throw new InvalidInputException("Coin value is invalid. Must be included in "+ Arrays.toString(coins.toArray()));
        }

        String username = this.authService.getUsernameFromAuth(authentication);
        User user = this.repository.findById(username).orElseThrow();
        user.setDeposit(user.getDeposit() + amount);
        this.repository.save(user);
    }

    public void reset(Authentication authentication) {
        String username = this.authService.getUsernameFromAuth(authentication);
        User user = read(username);
        user.setDeposit(0);
        this.repository.save(user);
    }
}
