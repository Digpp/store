package com.cy.store.service.ex;

public class InsertEmptyException extends ServiceException{
    public InsertEmptyException() {
        super();
    }

    public InsertEmptyException(String message) {
        super(message);
    }

    public InsertEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertEmptyException(Throwable cause) {
        super(cause);
    }

    public InsertEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
