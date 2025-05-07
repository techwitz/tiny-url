package org.techwitz.config;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.techwitz.exception.MaxAttemptsExceededException;
import org.techwitz.exception.TinyUrlException;
import org.techwitz.exception.UrlExpiredException;
import org.techwitz.exception.UrlNotFoundException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Map;

public class GlobalExceptionHandler {
    // Exception handlers - these need to be updated for reactive handling

    @ServerExceptionMapper
    public Uni<Response> handleUrlNotFoundException(UrlNotFoundException ex) {
        return Uni.createFrom().item(
                Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", ex.getMessage()))
                        .build()
        );
    }

    @ServerExceptionMapper
    public Uni<Response> handleUrlExpiredException(UrlExpiredException ex) {
        return Uni.createFrom().item(
                Response.status(Response.Status.GONE)
                        .entity(Map.of("error", ex.getMessage()))
                        .build()
        );
    }

    @ServerExceptionMapper
    public Uni<Response> handleMaxAttemptsExceededException(MaxAttemptsExceededException ex) {
        return Uni.createFrom().item(
                Response.status(Response.Status.TOO_MANY_REQUESTS)
                        .entity(Map.of("error", ex.getMessage()))
                        .build()
        );
    }

    @ServerExceptionMapper
    public Uni<Response> handleTinyUrlException(TinyUrlException ex) {
        return Uni.createFrom().item(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", ex.getMessage()))
                        .build()
        );
    }
}
