package org.techwitz.interceptor;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Provider
public class LoggingFilter {

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "X-Request-ID";

    @ServerRequestFilter
    public void logRequestFilter(ContainerRequestContext requestContext) throws IOException {
        // Generate or get request ID
        String requestId = requestContext.getHeaderString(REQUEST_ID);
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
            requestContext.getHeaders().add(REQUEST_ID, requestId);
        }

        // Store request ID in MDC for correlated logging
        org.jboss.logging.MDC.put("requestId", requestId);

        // Log request details
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String contentType = requestContext.getHeaderString("Content-Type");

        StringBuilder requestLogBuilder = new StringBuilder();
        requestLogBuilder.append("Incoming request | ")
                .append("ID: ").append(requestId).append(" | ")
                .append("Method: ").append(method).append(" | ")
                .append("Path: ").append(path).append(" | ")
                .append("Content-Type: ").append(contentType);

        // Log headers if needed (optional)
        if (LOG.isDebugEnabled()) {
            requestLogBuilder.append(" | Headers: ").append(requestContext.getHeaders());
        }

        // Log payload for POST, PUT methods
        if (("POST".equals(method) || "PUT".equals(method)) && contentType != null &&
                contentType.contains("application/json")) {

            // Read the entity stream
            byte[] entityBytes = requestContext.getEntityStream().readAllBytes();
            String entity = new String(entityBytes, StandardCharsets.UTF_8);

            // Need to reset the entity stream for further processing
            requestContext.setEntityStream(new ByteArrayInputStream(entityBytes));

            // Append payload to log (consider truncating for large payloads)
            if (entity.length() > 1000) {
                requestLogBuilder.append(" | Payload: ").append(entity.substring(0, 997)).append("...");
            } else {
                requestLogBuilder.append(" | Payload: ").append(entity);
            }
        }

        LOG.info(requestLogBuilder.toString());
    }

    @ServerResponseFilter
    public void logResponseFilter(ContainerRequestContext requestContext,
                                  ContainerResponseContext responseContext) {
        // Get request ID from MDC
        String requestId = (String) org.jboss.logging.MDC.get("requestId");

        // Log response details
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        int status = responseContext.getStatus();

        StringBuilder responseLogBuilder = new StringBuilder();
        responseLogBuilder.append("Outgoing response | ")
                .append("ID: ").append(requestId).append(" | ")
                .append("Method: ").append(method).append(" | ")
                .append("Path: ").append(path).append(" | ")
                .append("Status: ").append(status);

        // Log response entity if available and not too large
        Object entity = responseContext.getEntity();
        if (entity != null) {
            String entityStr = entity.toString();
            if (entityStr.length() > 1000) {
                responseLogBuilder.append(" | Response: ").append(entityStr.substring(0, 997)).append("...");
            } else {
                responseLogBuilder.append(" | Response: ").append(entityStr);
            }
        }

        LOG.info(responseLogBuilder.toString());

        // Clear MDC
        org.jboss.logging.MDC.clear();
    }
}
