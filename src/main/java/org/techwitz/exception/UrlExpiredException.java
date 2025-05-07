package org.techwitz.exception;

public class UrlExpiredException extends TinyUrlException {
    public UrlExpiredException(String message) {
        super(message);
    }
}
