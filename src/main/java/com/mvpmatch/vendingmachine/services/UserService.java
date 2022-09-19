package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserRepository repository;

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
}
