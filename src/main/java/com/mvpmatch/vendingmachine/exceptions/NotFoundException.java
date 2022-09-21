package com.mvpmatch.vendingmachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, String entityId) {
        super(entityName + " with id : " + entityId + " does not exist");
    }
}
