package com.ptb.uranus.sdk.exception;


/**
 * Created by eric on 16/4/21.
 */
public class ArgsNotFoundException extends RuntimeException {
    public ArgsNotFoundException(String format, Exception e) {
        super(format, e);
    }
    public ArgsNotFoundException(String message) {
        super(message);
    }
}
