package com.mvpmatch.vendingmachine;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpmatch.vendingmachine.daos.ProductRepository;
import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyTest {
    @Autowired
    private MockMvc mvc;


    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    private TransactionInput transactionInputTooExpensive = new TransactionInput();
    private TransactionInput transactionInputLowStock = new TransactionInput();
    private TransactionInput transactionInputWithUnknownProduct = new TransactionInput();
    private TransactionInput transactionInputRegular = new TransactionInput();

    private TransactionResult expectedResult = new TransactionResult();

    @BeforeAll
    public void init() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setDeposit(100);
        user.setRole(Role.valueOf("ROLE_BUYER"));

        Product product1 = new Product();
        product1.setProductName("product1");
        product1.setCost(50);
        product1.setAmountAvailable(2);

        Product product2 = new Product();
        product2.setProductName("product2");
        product2.setCost(10);
        product2.setAmountAvailable(9);

        productRepository.save(product1);
        productRepository.save(product2);
        userRepository.save(user);

        this.transactionInputTooExpensive.setProductPurchases(List.of(
                new ProductPurchase("product1", 1),
                new ProductPurchase("product2", 6)));

        this.transactionInputLowStock.setProductPurchases(List.of(
                new ProductPurchase("product2", 10)
        ));

        this.transactionInputWithUnknownProduct.setProductPurchases(List.of(
                new ProductPurchase("product3", 1)
        ));

        this.transactionInputRegular.setProductPurchases(List.of(
                new ProductPurchase("product1", 1),
                new ProductPurchase("product2", 4)
        ));

        this.expectedResult = new TransactionResult();

        expectedResult.setTotalSpent(90);
        expectedResult.setChange(List.of(new Change(10, 1)));
        expectedResult.setProductsPurchased(transactionInputRegular.getProductPurchases());


    }

    @Test
    @Order(1)
    public void transactionInputTooExpensive_shouldFailWith400() throws Exception {
        mvc.perform(
                        post("/buy")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionInputTooExpensive))
                )
                .andExpect(status().isBadRequest());

        assert userRepository.findById("user").get().getDeposit() == 100;
        assert productRepository.findById("product1").get().getAmountAvailable() == 2;
        assert productRepository.findById("product2").get().getAmountAvailable() == 9;
    }

    @Test
    @Order(2)
    public void transactionInputLowStock_shouldFailWith400() throws Exception {
        mvc.perform(
                        post("/buy")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionInputLowStock))
                )
                .andExpect(status().isBadRequest());

        assert userRepository.findById("user").get().getDeposit() == 100;
        assert productRepository.findById("product1").get().getAmountAvailable() == 2;
        assert productRepository.findById("product2").get().getAmountAvailable() == 9;
    }

    @Test
    @Order(3)
    public void transactionUnknownProduct_shouldFailWith404() throws Exception {
        mvc.perform(
                        post("/buy")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionInputWithUnknownProduct))
                )
                .andExpect(status().isNotFound());

        assert userRepository.findById("user").get().getDeposit() == 100;
        assert productRepository.findById("product1").get().getAmountAvailable() == 2;
        assert productRepository.findById("product2").get().getAmountAvailable() == 9;
    }

    @Test
    @Order(4)
    public void regularTransaction_shouldSucceedWith200() throws Exception {
        mvc.perform(
                        post("/buy")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionInputRegular))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(this.expectedResult)));

        assert userRepository.findById("user").get().getDeposit() == 0;
        assert productRepository.findById("product1").get().getAmountAvailable() == 1;
        assert productRepository.findById("product2").get().getAmountAvailable() == 5;
    }



}

