package org.techwitz.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class Guard {
    /**
     * Checks if a string is not empty.
     *
     * @param string  the string to check
     * @param message the error message to be thrown if the string is empty
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void notEmpty(@Nullable String string, String message) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given array is not empty.
     *
     * @param array   the array to check
     * @param message the error message to be thrown if the array is empty
     * @throws IllegalArgumentException if the array is null or empty
     */
    public static void notEmpty(@Nullable Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given iterable is not empty.
     *
     * @param iterable the iterable to check
     * @param message  the error message to be thrown if the iterable is empty
     * @throws IllegalArgumentException if the iterable is null or empty
     */
    public static void isNotEmpty(@Nullable Iterable<?> iterable, String message) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given condition is true. If the condition is false, an IllegalArgumentException is thrown with the specified message.
     *
     * @param condition the condition to check
     * @param message   the error message to include in the exception
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given condition is false. If the condition is true, an IllegalArgumentException is thrown with the specified message.
     *
     * @param condition the condition to check
     * @param message   the message to include in the exception if the condition is true
     * @throws IllegalArgumentException if the condition is true
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if a string is not null or empty.
     *
     * @param string  the string to check
     * @param message the error message to throw if the string is null or empty
     * @throws IllegalArgumentException if the string is null or empty
     */
    // @Contract(value = "null, _ -> fail;", pure = true)
    public static void isNotNullOrEmpty(@Nullable String string, String message) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static @Nonnull String requireNonNull(@Nullable String value, @Nonnull String defaultValue) {
        return value == null || isEmpty(value) ? defaultValue : value;
    }

    public static @Nonnull String requireNonNullOrWhiteSpace(@Nullable String value, @Nonnull String defaultValue) {
        return value == null || StringUtils.isBlank(value) ? defaultValue : value;
    }

    /**
     * Checks if the given iterable is null or empty.
     *
     * @param iterable the iterable to check
     * @param message  the error message to be thrown if the iterable is not null or empty
     * @throws IllegalArgumentException if the iterable is not null or empty
     */
    public static void isNotNullOrEmpty(@Nullable Iterable<?> iterable, String message) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given condition is true. If the condition is false, an IllegalArgumentException is thrown with the specified message.
     *
     * @param condition the condition to check
     * @param message   the error message to be included in the exception
     * @param args      optional arguments to be formatted into the error message
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition, String message, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks if the given condition is false. If the condition is true, an IllegalArgumentException is thrown with the specified message.
     *
     * @param condition the condition to check
     * @param message   the error message to be included in the exception
     * @param args      the arguments to be formatted into the error message
     * @throws IllegalArgumentException if the condition is true
     */
    public static void isFalse(boolean condition, String message, Object... args) {
        if (condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks if a string is not null or empty.
     * Throws an IllegalArgumentException if the string is null or empty.
     *
     * @param string  the string to check
     * @param message the error message to be thrown if the string is null or empty
     * @param args    the arguments to be formatted into the error message
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable String string, String message, Object... args) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks if the given array is not null or empty.
     * Throws an IllegalArgumentException if the array is null or empty.
     *
     * @param array   the array to check
     * @param message the error message to be thrown if the array is null or empty
     * @param args    the arguments to be formatted into the error message
     * @throws IllegalArgumentException if the array is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable Object[] array, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static <T> void isNotNullOrEmpty(@Nullable T[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the given iterable is not null or empty.
     * Throws an IllegalArgumentException if the iterable is null or empty.
     *
     * @param iterable the iterable to check
     * @param message  the error message to be thrown if the iterable is null or empty
     * @param args     optional arguments to be used in the error message
     * @throws IllegalArgumentException if the iterable is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable Iterable<?> iterable, String message, Object... args) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks if a string is not null or empty.
     *
     * @param string  the string to check
     * @param message the error message to be thrown if the string is null or empty
     * @param arg     the argument to be included in the error message
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable String string, String message, Object arg) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    /**
     * Checks if the given array is not null or empty.
     * Throws an IllegalArgumentException with the specified message and argument if the array is null or empty.
     *
     * @param array   the array to check
     * @param message the error message to be used in the exception
     * @param arg     the argument to be used in the exception message
     * @throws IllegalArgumentException if the array is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable Object[] array, String message, Object arg) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    /**
     * Checks if the given iterable is not null or empty.
     * Throws an IllegalArgumentException with the specified message and argument if the iterable is null or empty.
     *
     * @param iterable the iterable to check
     * @param message  the error message to be used in the exception
     * @param arg      the argument to be used in the exception message
     * @throws IllegalArgumentException if the iterable is null or empty
     */
    public static void isNotNullOrEmpty(@Nullable Iterable<?> iterable, String message, Object arg) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    /**
     * Checks if the given expression is true. If the expression is false, an IllegalArgumentException is thrown with the specified message.
     *
     * @param expression the boolean expression to check
     * @param message    the error message to include in the exception
     * @throws IllegalArgumentException if the expression is false
     */
    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks the given expression and throws an IllegalArgumentException if it evaluates to false.
     *
     * @param expression the boolean expression to check
     * @param message    the error message to be included in the exception
     * @param args       optional arguments to be formatted into the error message
     * @throws IllegalArgumentException if the expression evaluates to false
     */
    public static void check(final boolean expression, final String message, final Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks the given expression and throws an IllegalArgumentException with the specified message and argument if the expression is false.
     *
     * @param expression the boolean expression to check
     * @param message    the error message to include in the exception
     * @param arg        the argument to include in the exception message
     * @throws IllegalArgumentException if the expression is false
     */
    public static void check(final boolean expression, final String message, final Object arg) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    /**
     * Checks if the given value is within the specified range.
     *
     * @param value         the value to be checked
     * @param lowInclusive  the lower bound of the range (inclusive)
     * @param highInclusive the upper bound of the range (inclusive)
     * @param message       the error message to be thrown if the value is out of range
     * @return the value if it is within the range
     * @throws IllegalArgumentException if the value is out of range
     */
    public static int checkRange(final int value, final int lowInclusive, final int highInclusive,
                                 final String message) {
        if (value < lowInclusive || value > highInclusive) {
            throw illegalArgumentException("%s: %d is out of range [%d, %d]", message, value,
                                           lowInclusive, highInclusive);
        }
        return value;
    }

    /**
     * Checks if a given value is within a specified range.
     *
     * @param value         the value to be checked
     * @param lowInclusive  the lower bound of the range (inclusive)
     * @param highInclusive the upper bound of the range (inclusive)
     * @param message       the error message to be thrown if the value is out of range
     * @return the value if it is within the range
     * @throws IllegalArgumentException if the value is out of range
     */
    public static long checkRange(final long value, final long lowInclusive, final long highInclusive,
                                  final String message) {
        if (value < lowInclusive || value > highInclusive) {
            throw illegalArgumentException("%s: %d is out of range [%d, %d]", message, value,
                                           lowInclusive, highInclusive);
        }
        return value;
    }

    /**
     * Constructs an IllegalArgumentException with the specified detail message.
     *
     * @param format the format string for the detail message
     * @param args   the arguments referenced by the format string
     * @return the newly constructed IllegalArgumentException
     */
    private static IllegalArgumentException illegalArgumentException(final String format, final Object... args) {
        return new IllegalArgumentException(String.format(format, args));
    }

    /**
     * Constructs an IllegalArgumentException with the specified detail message.
     *
     * @param name the name of the argument that caused the exception
     * @return an IllegalArgumentException with the specified detail message
     */
    private static IllegalArgumentException illegalArgumentExceptionNotEmpty(final String name) {
        return new IllegalArgumentException(name + " must not be empty");
    }

    /**
     * Checks if the given argument is not empty.
     *
     * @param <T>      the type of the argument, which must extend CharSequence
     * @param argument the argument to check
     * @param name     the name of the argument
     * @return the argument if it is not empty
     * @throws IllegalArgumentException if the argument is empty
     */
    public static <T extends CharSequence> T notEmpty(@Nullable final T argument, final String name) throws IllegalArgumentException, NullPointerException {
        if (argument == null || isEmpty(argument)) {
            throw illegalArgumentExceptionNotEmpty(name);
        }
        return argument;
    }

    /**
     * Checks if a collection is not empty.
     *
     * @param <E>      the type of elements in the collection
     * @param <T>      the type of collection
     * @param argument the collection to check
     * @param name     the name of the collection
     * @return the non-empty collection
     * @throws IllegalArgumentException if the collection is empty
     */
    public static <E, T extends Collection<E>> T notEmpty(@Nullable final T argument, final String name) throws IllegalArgumentException, NullPointerException {
        if (argument == null || isEmpty(argument)) {
            throw illegalArgumentExceptionNotEmpty(name);
        }
        return argument;
    }

    /**
     * Checks if the given argument is not empty.
     *
     * @param <T>      the type of the argument
     * @param argument the argument to check
     * @param name     the name of the argument
     * @return the argument if it is not empty
     * @throws IllegalArgumentException if the argument is empty
     */
    public static <T> T notEmpty(@Nullable final T argument, final String name) throws IllegalArgumentException, NullPointerException {
        if (argument == null || isEmpty(argument)) {
            throw illegalArgumentExceptionNotEmpty(name);
        }
        return argument;
    }

    /**
     * Ensures that the given integer value is not negative.
     *
     * @param n    the integer value to check
     * @param name the name of the value being checked
     * @return the non-negative integer value
     * @throws IllegalArgumentException if the value is negative
     */
    public static int notNegative(final int n, final String name) {
        if (n < 0) {
            throw illegalArgumentException("%s must not be negative: %d", name, n);
        }
        return n;
    }

    /**
     * Ensures that the given value is not negative.
     *
     * @param n    the value to check
     * @param name the name of the value
     * @return the non-negative value
     * @throws IllegalArgumentException if the value is negative
     */
    public static long notNegative(final long n, final String name) {
        if (n < 0) {
            throw illegalArgumentException("%s must not be negative: %d", name, n);
        }
        return n;
    }

    /**
     * Ensures that the given number is not negative.
     *
     * @param <T>    the type of the number
     * @param number the number to check
     * @param name   the name of the number
     * @return the given number if it is not negative
     */
    public static <T extends Number> T notNegative(final T number, final String name) {
        notNegative(number.longValue(), name);
        return number;
    }

    /**
     * <p>Validate that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Args.notNull(myObject, "The object must not be null");</pre>
     *
     * @param <T>      the object type
     * @param argument the object to check
     * @param name     the {@link String} exception message if invalid, not null
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     */
    public static <T> T isNotNull(@Nullable final T argument, final String name) throws IllegalArgumentException {
        if (argument == null)
            throw new IllegalArgumentException("The " + name + " must not be null");
        return argument;
    }

    /**
     * <p>Checks if an Object is empty or null.</p>
     * <p>
     * The following types are supported:
     * <ul>
     * <li>{@link CharSequence}: Considered empty if its length is zero.</li>
     * <li>{@code Array}: Considered empty if its length is zero.</li>
     * <li>{@link Collection}: Considered empty if it has zero elements.</li>
     * <li>{@link Map}: Considered empty if it has zero key-value mappings.</li>
     * </ul>
     *
     * <pre>
     * Args.isEmpty(null)             = true
     * Args.isEmpty("")               = true
     * Args.isEmpty("ab")             = false
     * Args.isEmpty(new int[]{})      = true
     * Args.isEmpty(new int[]{1,2,3}) = false
     * Args.isEmpty(1234)             = false
     * </pre>
     *
     * @param object the {@code Object} to test, may be {@code null}
     * @return {@code true} if the object has a supported type and is empty or null,
     * {@code false} otherwise
     * @since 5.1
     */
    public static boolean isEmpty(final @Nullable Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).isEmpty();
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).isEmpty();
        }
        return false;
    }

    /**
     * Ensures that the given integer is positive.
     *
     * @param n    the integer to check
     * @param name the name of the integer parameter
     * @return the positive integer
     * @throws IllegalArgumentException if the integer is negative or zero
     */
    public static int positive(final int n, final String name) {
        if (n <= 0) {
            throw illegalArgumentException("%s must not be negative or zero: %d", name, n);
        }
        return n;
    }

    /**
     * Ensures that the given value is positive.
     *
     * @param n    the value to check
     * @param name the name of the value
     * @return the positive value
     * @throws IllegalArgumentException if the value is negative or zero
     */
    public static long positive(final long n, final String name) {
        if (n <= 0) {
            throw illegalArgumentException("%s must not be negative or zero: %d", name, n);
        }
        return n;
    }

    /**
     * Ensures that the given number is positive.
     *
     * @param <T>    the type of the number
     * @param number the number to check
     * @param name   the name of the number
     * @return the same number if it is positive
     */
    public static <T extends Number> T positive(final T number, final String name) {
        positive(number.longValue(), name);
        return number;
    }

    /**
     * Checks if the given argument is not empty.
     *
     * @param <T>      the type of the argument
     * @param argument the argument to check
     * @param name     the name of the argument
     * @param args     additional arguments for formatting the error message
     * @return the argument if it is not empty
     * @throws IllegalArgumentException if the argument is empty
     */
    public static <T> T notEmpty(final T argument, final String name, final Object... args) throws IllegalArgumentException, NullPointerException {
        isNotNull(argument, name);
        isNotNull(argument, name);
        if (isEmpty(argument)) {
            throw illegalArgumentExceptionNotEmpty(String.format(name, args));
        }
        return argument;
    }

    /**
     * Checks if the given argument is not empty.
     *
     * @param <T>      the type of the argument
     * @param argument the argument to check
     * @param name     the name of the argument
     * @param arg      the additional information about the argument
     * @return the argument if it is not empty
     * @throws IllegalArgumentException if the argument is empty
     */
    public static <T> T notEmpty(@Nullable final T argument, final String name, final Object arg) throws IllegalArgumentException, NullPointerException {
        if (argument == null || isEmpty(argument)) {
            throw illegalArgumentExceptionNotEmpty(String.format(name, arg));
        }
        return argument;
    }

    public static void isGreaterThanZero(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanZero(long value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanZero(double value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanZero(float value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanZero(short value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanZero(byte value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(int value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(long value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(double value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(float value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(short value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanZero(byte value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(long value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(double value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(float value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(short value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(byte value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(int value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(long value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(double value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(float value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(short value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(byte value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNonNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(int value, int target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(long value, long target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(double value, double target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(float value, float target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(short value, short target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThan(byte value, byte target, String message) {
        if (value <= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(int value, int target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(long value, long target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(double value, double target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(float value, float target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(short value, short target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isGreaterThanOrEqual(byte value, byte target, String message) {
        if (value < target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(int value, int target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(long value, long target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(double value, double target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(float value, float target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(short value, short target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThan(byte value, byte target, String message) {
        if (value >= target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(int value, int target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(long value, long target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(double value, double target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(float value, float target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(short value, short target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(byte value, byte target, String message) {
        if (value > target) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isLessThanOrEqual(byte value, byte target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThanOrEqual(short value, short target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThanOrEqual(float value, float target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThanOrEqual(double value, double target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThanOrEqual(long value, long target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThanOrEqual(int value, int target) {
        if (value > target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(byte value, byte target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(short value, short target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(float value, float target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(double value, double target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(long value, long target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isLessThan(int value, int target) {
        if (value >= target) {
            throw new IllegalArgumentException("Value must be less than or equal to " + target);
        }
    }

    public static void isGreaterThanZero(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isGreaterThanZero(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isGreaterThanZero(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isGreaterThanZero(float value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isGreaterThanZero(short value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isGreaterThanZero(byte value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
    }

    public static void isLessThanZero(int value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isLessThanZero(long value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isLessThanZero(double value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isLessThanZero(float value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isLessThanZero(short value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isLessThanZero(byte value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be less than zero");
        }
    }

    public static void isPositive(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isPositive(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isPositive(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isPositive(float value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isPositive(short value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isPositive(byte value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(int value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(long value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(double value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(float value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(short value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNegative(byte value) {
        if (value >= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public static void isNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be non-negative");
        }
    }

    public static void isGreaterThan(int value, int target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThan(long value, long target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThan(double value, double target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThan(float value, float target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThan(short value, short target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThan(byte value, byte target) {
        if (value <= target) {
            throw new IllegalArgumentException("Value must be greater than " + target);
        }
    }

    public static void isGreaterThanOrEqual(int value, int target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isGreaterThanOrEqual(long value, long target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isGreaterThanOrEqual(double value, double target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isGreaterThanOrEqual(float value, float target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isGreaterThanOrEqual(short value, short target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isGreaterThanOrEqual(byte value, byte target) {
        if (value < target) {
            throw new IllegalArgumentException("Value must be greater than or equal to " + target);
        }
    }

    public static void isNumber(Object number) {
        if (!(number instanceof Number)) {
            throw new IllegalArgumentException("Value must be a number");
        }
    }

    public static void isNumber(Object number, String paramName) {
        if (!(number instanceof Number)) {
            throw new IllegalArgumentException("Value must be a number for parameter: " + paramName);
        }
    }
}
