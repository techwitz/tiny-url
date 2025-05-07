package org.techwitz.util;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import java.util.function.Supplier;

/**
 * Utility class for logging operations.
 */
public class LoggingUtils {

    private static final Logger LOG = Logger.getLogger(LoggingUtils.class);

    /**
     * Logs an operation with timing information.
     *
     * @param <T> The return type of the operation
     * @param operationName The name of the operation to log
     * @param supplier The operation to execute
     * @return The result of the operation
     */
    public static <T> Uni<T> logOperation(String operationName, Supplier<Uni<T>> supplier) {
        long startTime = System.currentTimeMillis();

        LOG.infof("Starting operation: %s", operationName);

        return supplier.get()
                .onItem().invoke(item -> {
                    long endTime = System.currentTimeMillis();
                    LOG.infof("Completed operation: %s in %d ms with result: %s",
                            operationName, (endTime - startTime), item);
                })
                .onFailure().invoke(failure -> {
                    long endTime = System.currentTimeMillis();
                    LOG.errorf(failure, "Failed operation: %s in %d ms with error: %s",
                            operationName, (endTime - startTime), failure.getMessage());
                });
    }

    /**
     * Logs service metrics for an operation.
     *
     * @param <T> The return type of the operation
     * @param operationName The name of the operation to log
     * @param metadata Additional metadata to log
     * @param supplier The operation to execute
     * @return The result of the operation
     */
    public static <T> Uni<T> logServiceMetrics(String operationName, String metadata, Supplier<Uni<T>> supplier) {
        String requestId = (String) org.jboss.logging.MDC.get("requestId");

        LOG.infof("SERVICE_METRIC | Operation: %s | RequestID: %s | Metadata: %s",
                operationName, requestId, metadata);

        return supplier.get();
    }
}

