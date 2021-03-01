package com.vlados.exception.store_exc;


import com.vlados.exception.StoreException;

public class UserDoesntExist extends StoreException {
    public UserDoesntExist(String message) {
        super(message);
    }
}
