package com.mvpmatch.vendingmachine.daos;

import com.mvpmatch.vendingmachine.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
