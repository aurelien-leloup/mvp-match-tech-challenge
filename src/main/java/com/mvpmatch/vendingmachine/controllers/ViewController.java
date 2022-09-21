package com.mvpmatch.vendingmachine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping({
            "/login",
            "/create",
            "/seller",
            "/buyer"})
    public String index() {
        return "forward:/index.html";
    }

}
