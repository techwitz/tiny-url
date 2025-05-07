package org.techwitz.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;

/**
 * Utility class for generating short codes for tiny URLs.
 */
@ApplicationScoped
public class ShortCodeGenerator {
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

    /**
     * Generates a random short code of the specified length.
     *
     * @param length The length of the short code to generate
     * @return A random short code
     */
    public String generate(int length) {
        StringBuilder shortCode = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            shortCode.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return shortCode.toString();
    }
}

