package com.mvpmatch.vendingmachine.controllers;

import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.models.Product;
import com.mvpmatch.vendingmachine.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public Product create(@RequestBody Product product, Authentication authentication) {
        return productService.create(product, authentication);
    }


    @GetMapping("/{productName}")
    @ResponseBody
    Product read(@PathVariable String productName) {
        return this.productService.read(productName);
    }

    @GetMapping
    @ResponseBody
    List<Product> list() {
        return this.productService.list();
    }

    @PutMapping
    Product update(@RequestBody Product product, Authentication authentication) {
        return this.productService.update(product, authentication);
    }

    @DeleteMapping("/{productName}")
    void delete(@PathVariable String productName, Authentication authentication) {
        this.productService.delete(productName, authentication);
    }



}
