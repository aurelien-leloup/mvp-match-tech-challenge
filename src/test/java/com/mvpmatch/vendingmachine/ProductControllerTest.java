package com.mvpmatch.vendingmachine;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpmatch.vendingmachine.models.Product;
import com.mvpmatch.vendingmachine.services.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    ObjectMapper objectMapper;

    private Product validProduct;
    private Product invalidProductQuantity;
    private Product invalidProductCost;

    private Product expectedResultProduct;

    @BeforeAll
    public void init() {
        this.validProduct = new Product();
        validProduct.setAmountAvailable(2);
        validProduct.setProductName("the product");
        validProduct.setCost(100);

        this.expectedResultProduct = new Product();
        expectedResultProduct.setAmountAvailable(2);
        expectedResultProduct.setProductName("the product");
        expectedResultProduct.setCost(100);
        expectedResultProduct.setSellerId("user");

        this.invalidProductQuantity = new Product();
        invalidProductQuantity.setAmountAvailable(-100);
        invalidProductQuantity.setCost(100);
        invalidProductQuantity.setProductName("the product 2");

        this.invalidProductCost = new Product();
        invalidProductCost.setAmountAvailable(10);
        invalidProductCost.setCost(1);
        invalidProductCost.setProductName("the product 3");

    }


    @Test
    @Order(1)
    public void createValidProductWithAuth_shouldSucceedWith200() throws Exception {
        mvc.perform(
                        post("/product")
                                .with(user("user").roles("SELLER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(this.validProduct))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(this.expectedResultProduct)));
    }

    @Test
    @Order(2)
    public void tryToCreateExistingProduct_shouldFailWith400() throws Exception {
        mvc.perform(
                post("/product")
                        .with(user("user").roles("SELLER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.validProduct))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void tryToCreateInvalidProductCost_shouldFailWith400() throws Exception {
        mvc.perform(
                post("/product")
                        .with(user("user").roles("SELLER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.invalidProductCost))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void tryToCreateInvalidProductQuantity_shouldFailWith400() throws Exception {
        mvc.perform(
                post("/product")
                        .with(user("user").roles("SELLER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.invalidProductQuantity))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void tryToCreateProductWithoutAuth_shouldFailWith403() throws Exception {
        mvc.perform(
                post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.validProduct))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void tryToCreateProductWithRoleBuyer_shouldFailWith403() throws Exception {
        mvc.perform(
                post("/product")
                        .with(user("user").roles("BUYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.validProduct))
        ).andExpect(status().isForbidden());
    }

}
