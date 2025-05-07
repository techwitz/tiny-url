package org.techwitz.util;

import jakarta.annotation.Nullable;

import java.io.File;
import java.util.Random;
import java.util.UUID;

public class StringUtils {
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String WHITE_SPACE = " ";
    public static final char PATH_SEPARATOR_CHAR = File.separatorChar;
    public static final String PATH_SEPARATOR = String.valueOf(PATH_SEPARATOR_CHAR);

    public static final String EMPTY = "";
    /**
     * A String for linefeed LF ("\n").
     *
     * @see <a href=
     * "https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.6">JLF:
     * Escape Sequences
     * for Character and String Literals</a>
     */
    public static final String LF = "\n";
    /**
     * A String for carriage return CR ("\r").
     *
     * @see <a href=
     * "https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.6">JLF:
     * Escape Sequences
     * for Character and String Literals</a>
     */
    public static final String CR = "\r";

    /**
     * Check if the String value is null, empty or contains only whitespace characters.
     *
     * @param value String value
     * @return if the string is blank
     */
    public static boolean isBlank(@Nullable final String value) {
        return value == null ||
                value.isEmpty() ||
                value.trim().isEmpty() ||
                ("" + value).equalsIgnoreCase("null") ||
                ("" + value).equalsIgnoreCase("undefined");
    }

    public static boolean isNotBlank(@Nullable final String value) {
        return !isBlank(value);
    }

    /**
     * Checks if a CharSequence is empty ("") or null.
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(final @Nullable CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean isNotEmpty(final @Nullable CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(final @Nullable String cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean isNotEmpty(final @Nullable String cs) {
        return !isEmpty(cs);
    }

    public static String randomString() {
        byte stringLength = 10;
        return randomString(stringLength);
    }

    public static String randomString(byte stringLength) {
        if (stringLength < 5) {
            stringLength = 5;
        }
        if (stringLength > 36) {
            stringLength = 36;
        }

        String rndString = UUID.randomUUID().toString().replaceAll("-", "");
        int index = 0;

        boolean hasLetter = false;
        for (int i = 0; i < rndString.length(); i++) {
            if (Character.isLetter(rndString.charAt(i))) {
                hasLetter = true;
                break;
            }
        }

        if (!hasLetter) {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            for(int ctr = 1; ctr <= 3; ctr++) {
                int randomIndex = random.nextInt(rndString.length());
                var oldChar = rndString.charAt(randomIndex);
                rndString = rndString.replace(oldChar, ALPHABETS.charAt(randomIndex));
            }
        }

        return rndString.substring(0, stringLength).toLowerCase();
    }

    public static String firstNonBlank(@Nullable String... strings) {
        if (strings == null) {
            return "";
        }
        for (String s : strings) {
            if (StringUtils.isNotBlank(s)) {
                return s;
            }
        }
        return "";
    }
}
