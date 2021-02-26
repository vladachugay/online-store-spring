package com.vlados.exception.store_exc;


import com.vlados.exception.StoreException;

public class CantCreateOrderException extends StoreException {
    public CantCreateOrderException(String message) {
        super(message);
    }
}
