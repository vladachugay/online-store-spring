package com.vlados.exception.store_exc;


import com.vlados.exception.StoreException;

public class NotEnoughProductsException extends StoreException {
    public NotEnoughProductsException(String msg) {
        super(msg);
    }
}
