package com.mvpmatch.vendingmachine.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_BUYER, ROLE_SELLER;


    @Override
    public String getAuthority() {
        return name();
    }
}
