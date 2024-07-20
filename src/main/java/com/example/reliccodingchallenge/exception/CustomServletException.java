package com.example.reliccodingchallenge.exception;

/**
 * Custom Generic Exception to map errors.
 * @author Christopher Burnette / chrisburnette188@gmail.com
 */

public class CustomServletException extends RuntimeException {

    public CustomServletException(String message) {
        super(message);
    }


}
