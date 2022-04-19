package com.nesterov.expressions.service.exceptions;

public class ParenthesesMismatchException extends RuntimeException{

    public ParenthesesMismatchException(String message) {
        super(message);
    }
}
