package com.shopping.user.exception;

public class JwtTokenException extends Exception {
    public JwtTokenException(String errorMessage) {
        super(errorMessage);
    }
}
