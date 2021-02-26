package com.vlados.exception.store_exc.login_exc;

import com.vlados.exception.store_exc.LoginException;

public class UserIsLockedException extends LoginException {
    public UserIsLockedException(String message) {
        super(message);
    }
}
