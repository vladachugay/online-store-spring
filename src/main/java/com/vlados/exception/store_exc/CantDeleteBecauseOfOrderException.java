package com.vlados.exception.store_exc;

import com.vlados.exception.StoreException;

public class CantDeleteBecauseOfOrderException extends StoreException {
    public CantDeleteBecauseOfOrderException(String message) {
        super(message);
    }
}
