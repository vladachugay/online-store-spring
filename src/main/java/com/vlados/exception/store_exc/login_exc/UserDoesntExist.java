package com.vlados.exception.store_exc.login_exc;


import com.vlados.exception.store_exc.LoginException;

public class UserDoesntExist extends LoginException {
    public UserDoesntExist(String message) {
        super(message);
    }
}
