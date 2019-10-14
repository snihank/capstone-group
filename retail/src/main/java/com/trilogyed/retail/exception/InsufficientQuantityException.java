package com.trilogyed.retail.exception;

public class InsufficientQuantityException extends RuntimeException {

    public InsufficientQuantityException() {
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }
}
