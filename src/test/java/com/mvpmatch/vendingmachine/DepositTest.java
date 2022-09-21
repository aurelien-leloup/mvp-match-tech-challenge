package com.mvpmatch.vendingmachine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpmatch.vendingmachine.daos.ProductRepository;
import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.models.Product;
import com.mvpmatch.vendingmachine.models.Role;
import com.mvpmatch.vendingmachine.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepositTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    public void init() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setRole(Role.valueOf("ROLE_BUYER"));

        userRepository.save(user);
    }

    @Test
    @Order(1)
    public void validDeposit_shouldIncreaseDepositWith200() throws Exception {
        mvc.perform(
                        put("/deposit")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"value\": 100}")
                )
                .andExpect(status().isOk());

        assert userRepository.findById("user").get().getDeposit() == 100;
    }

    @Test
    @Order(2)
    public void invalidDeposit_shouldFailWith400() throws Exception {
        mvc.perform(
                        put("/deposit")
                                .with(user("user").roles("BUYER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"value\": 765}")
                )
                .andExpect(status().isBadRequest());

        assert userRepository.findById("user").get().getDeposit() == 100;
    }

    @Test
    @Order(3)
    public void depositAsSeller_shouldFailWith403() throws Exception {
        mvc.perform(
                        put("/deposit")
                                .with(user("user").roles("SELLER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"value\": 100}")
                )
                .andExpect(status().isForbidden());

        assert userRepository.findById("user").get().getDeposit() == 100;
    }


}
