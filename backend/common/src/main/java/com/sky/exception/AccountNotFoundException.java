package com.sky.exception;

/**
 * Account Not Found Exception
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
