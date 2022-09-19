package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserRepository repository;

    @Autowired
    AuthService authService;

    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    public User read(String username) {
        return this.repository.findById(username).orElseThrow();
    }

    public void delete(String username) {
        User user = this.repository.findById(username).orElseThrow();
        this.repository.delete(user);
    }

    public void setDeposit(Integer amount, Authentication authentication) {
        if (!List.of(5, 10, 20, 50, 100).contains(amount)) {
            throw new InvalidInputException();
        }

        String username = this.authService.getUsernameFromAuth(authentication);
        User user = this.repository.findById(username).orElseThrow();
        user.setDeposit(user.getDeposit() + amount);
        this.repository.save(user);
    }
}
