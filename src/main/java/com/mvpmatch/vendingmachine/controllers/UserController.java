package com.mvpmatch.vendingmachine.controllers;

import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.models.User;
import com.mvpmatch.vendingmachine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @ResponseBody
    User create(@RequestBody User user) {
        checkUser(user);
        return this.userService.create(user);
    }

    @GetMapping("/{username}")
    @ResponseBody
    User read(@PathVariable String username) {
        return this.userService.read(username);
    }

    @PutMapping
    User update(@RequestBody User user) {
        checkUser(user);
        return this.userService.update(user);
    }

    @DeleteMapping("/{username}")
    void delete(@PathVariable String username) {
        this.userService.delete(username);
    }


    private void checkUser(User user) {
        if (user.getDeposit() < 0) {
            throw new InvalidInputException("Deposit cannot be negative");
        }
    }

}
