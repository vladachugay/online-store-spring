package com.vlados.exception.store_exc;


import com.vlados.exception.StoreException;

public class DuplicateProductNameException extends StoreException {
    public DuplicateProductNameException(String message) {
        super(message);
    }
}
