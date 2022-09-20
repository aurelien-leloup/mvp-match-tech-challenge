package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.ProductRepository;
import com.mvpmatch.vendingmachine.exceptions.UnauthorizedException;
import com.mvpmatch.vendingmachine.models.Product;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    AuthService authService;


    public Product create(Product product, Authentication authentication) {
        String sellerId = authService.getUsernameFromAuth(authentication);
        product.setSellerId(sellerId);

        if (product.getCost() % 5 != 0) {
            throw new RuntimeException("Product price has to be a multiple of 5");
        }


        return repository.save(product);
    }

    public void save(Product product) {
        this.repository.save(product);
    }

    public Product read(String productName) {
        return this.repository.findById(productName).orElseThrow();
    }

    public Product update(Product product, Authentication authentication) {
        Product productInDB = this.repository.findById(product.getProductName()).orElseThrow();
        String sellerIdDB = productInDB.getSellerId();
        String sellerIdAuth = authService.getUsernameFromAuth(authentication);

        if (!sellerIdAuth.equals(sellerIdDB)) {
            throw new UnauthorizedException();
        }

        return this.repository.save(product);
    }

    public void delete(String productName, Authentication authentication) {
        Product product = this.repository.findById(productName).orElseThrow();
        String sellerIdDB = product.getSellerId();
        String sellerIdAuth = authService.getUsernameFromAuth(authentication);

        if (!sellerIdAuth.equals(sellerIdDB)) {
            throw new UnauthorizedException();
        }

        this.repository.delete(product);
    }

    public List<Product> list() {
        return IterableUtils.toList(this.repository.findAll());
    }

}
