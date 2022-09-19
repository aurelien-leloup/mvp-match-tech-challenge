package com.mvpmatch.vendingmachine.controllers;

import com.mvpmatch.vendingmachine.models.AuthentifiedUser;
import com.mvpmatch.vendingmachine.models.LoginForm;
import com.mvpmatch.vendingmachine.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping
    public AuthentifiedUser login(@RequestBody LoginForm loginForm) {
        return authService.login(loginForm.getUsername(), loginForm.getPassword());
    }
}
