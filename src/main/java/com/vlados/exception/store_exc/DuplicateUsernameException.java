package com.vlados.exception.store_exc;


import com.vlados.exception.StoreException;

public class DuplicateUsernameException extends StoreException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
