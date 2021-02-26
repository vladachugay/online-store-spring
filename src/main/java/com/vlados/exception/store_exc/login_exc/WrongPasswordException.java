package com.vlados.exception.store_exc.login_exc;


import com.vlados.exception.store_exc.LoginException;

public class WrongPasswordException  extends LoginException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
