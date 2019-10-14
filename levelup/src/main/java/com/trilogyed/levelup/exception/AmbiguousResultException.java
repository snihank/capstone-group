package com.trilogyed.levelup.exception;


public class AmbiguousResultException extends RuntimeException {

    public AmbiguousResultException() {
    }

    public AmbiguousResultException(String message) {
        super(message);
    }
}
