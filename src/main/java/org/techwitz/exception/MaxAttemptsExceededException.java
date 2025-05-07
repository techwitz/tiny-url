package org.techwitz.exception;

public class MaxAttemptsExceededException extends TinyUrlException {
    public MaxAttemptsExceededException(String message) {
        super(message);
    }
}