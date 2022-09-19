package com.mvpmatch.vendingmachine.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity
public class User {
    @Column
    @Id
    private String username;

    @Column
    private String password;

    @Column
    private int deposit;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
}
