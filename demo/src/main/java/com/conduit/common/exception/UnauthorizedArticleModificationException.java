package com.conduit.common.exception;

public class UnauthorizedArticleModificationException extends RuntimeException {
    public UnauthorizedArticleModificationException(String message) {
        super(message);
    }
}
