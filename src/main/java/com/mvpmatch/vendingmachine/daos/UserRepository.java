package com.mvpmatch.vendingmachine.daos;

import com.mvpmatch.vendingmachine.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, String> {

}
