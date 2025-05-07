package org.techwitz.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;

import java.util.Arrays;

@Loggable
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    private static final Logger LOG = Logger.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        final String className = context.getTarget().getClass().getName();
        final String methodName = context.getMethod().getName();
        final Object[] parameters = context.getParameters();

        // Log method entry
        LOG.infof("Entering: %s.%s with parameters: %s",
                className, methodName, Arrays.toString(parameters));

        try {
            // Get the result and handle reactive types specially
            Object result = context.proceed();

            // For reactive results, we need to add logging to the reactive chain
            if (result instanceof Uni<?>) {
                return ((Uni<?>) result)
                        .onItem().invoke(item -> LOG.infof("Exiting: %s.%s with result: %s",
                                className, methodName, item))
                        .onFailure().invoke(failure -> LOG.errorf(failure,
                                "Failed execution: %s.%s with error: %s",
                                className, methodName, failure.getMessage()));
            } else {
                // For non-reactive results, log directly
                LOG.infof("Exiting: %s.%s with result: %s",
                        className, methodName, result);
                return result;
            }
        } catch (Exception e) {
            LOG.errorf(e, "Exception in: %s.%s with error: %s",
                    className, methodName, e.getMessage());
            throw e;
        }
    }
}
