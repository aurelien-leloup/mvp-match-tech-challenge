package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.ProductRepository;
import com.mvpmatch.vendingmachine.exceptions.InvalidInputException;
import com.mvpmatch.vendingmachine.exceptions.NotFoundException;
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
        checkProduct(product);

        try {
            read(product.getProductName());
        } catch (NotFoundException e) {
            String sellerId = authService.getUsernameFromAuth(authentication);
            product.setSellerId(sellerId);
            return repository.save(product);
        }

        throw new InvalidInputException("Product already exists");
    }

    public Product read(String productName) {
        return this.repository
                .findById(productName)
                .orElseThrow(() -> new NotFoundException("Product", productName));
    }

    public Product update(Product product, Authentication authentication) {
        checkProduct(product);
        Product productInDB = read(product.getProductName());
        String sellerIdDB = productInDB.getSellerId();
        String sellerIdAuth = authService.getUsernameFromAuth(authentication);

        if (!sellerIdAuth.equals(sellerIdDB)) {
            throw new UnauthorizedException("Product does not belong to authenticated user");
        }

        return this.repository.save(product);
    }

    public void save(Product product) {
        this.repository.save(product);
    }

    public void delete(String productName, Authentication authentication) {
        Product product = read(productName);
        String sellerIdDB = product.getSellerId();
        String sellerIdAuth = authService.getUsernameFromAuth(authentication);

        if (!sellerIdAuth.equals(sellerIdDB)) {
            throw new UnauthorizedException("Product does not belong to authenticated user");
        }

        this.repository.delete(product);
    }

    public List<Product> list() {
        return IterableUtils.toList(this.repository.findAll());
    }

    private void checkProduct(Product product) {
        if (product.getCost() <= 0) {
            throw new InvalidInputException("Product price has to be more than 0");
        }

        if (product.getCost() % 5 != 0) {
            throw new InvalidInputException("Product price has to be a multiple of 5");
        }

        if (product.getAmountAvailable() < 0) {
            throw new InvalidInputException("Product amount invalid");
        }
    }


}
