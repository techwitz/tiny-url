package org.techwitz.controller;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import org.techwitz.dto.TinyUrlRequest;
import org.techwitz.dto.TinyUrlResponse;
import org.techwitz.service.TinyUrlService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.techwitz.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tiny URL API", description = "Asynchronous operations for managing tiny URLs")
public class TinyUrlController {

    @Inject
    TinyUrlService tinyUrlService;

    /**
     * Creates a new tiny URL asynchronously.
     *
     * @param request The request containing original URL and configuration
     * @return Uni with Response containing the generated short URL
     */
    @POST
    @Path("/api/urls")
    @WithTransaction
    @Operation(
            summary = "Create a new tiny URL",
            description = "Creates a shortened URL with optional expiration time, usage limits, and attempt limits asynchronously"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Tiny URL created successfully",
                    content = @Content(schema = @Schema(implementation = TinyUrlResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @Counted(name = "tinyUrlCreations", description = "How many tiny URLs have been created")
    @Timed(name = "tinyUrlCreationTimer", description = "A measure of how long it takes to create a tiny URL")
    public Uni<Response> createTinyUrl(
            @RequestBody(
                    description = "URL details for shortening",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TinyUrlRequest.class))
            )
            @Valid @NotNull TinyUrlRequest request) {

        return tinyUrlService.createTinyUrl(request)
                .map(response -> Response.created(
                                     UriBuilder.fromResource(TinyUrlController.class)
                                             .path("/{shortCode}")
                                             .build(response.getShortUrl().substring(response.getShortUrl().lastIndexOf('/') + 1))
                             )
                             .entity(response)
                             .build()
                );
    }

    /**
     * Redirects to the original URL for a given short code asynchronously.
     *
     * @param shortCode The short code to resolve
     * @return Uni with Response redirecting to the original URL
     */
    @GET
    @WithSession
    @Path("/t/{shortCode}")
    @Operation(
            summary = "Redirect to the original URL",
            description = "Resolves a short code and redirects to the original URL while tracking usage and attempt statistics asynchronously"
    )
    @APIResponses({
            @APIResponse(responseCode = "302", description = "Redirect to original URL"),
            @APIResponse(responseCode = "404", description = "URL not found"),
            @APIResponse(responseCode = "410", description = "URL expired or usage limit exceeded"),
            @APIResponse(responseCode = "429", description = "Maximum number of attempts exceeded")
    })
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "tinyUrlRedirects", description = "How many redirects have been performed")
    public Uni<Response> redirectToOriginalUrl(
            @Parameter(
                    description = "Short code of the URL",
                    required = true,
                    example = "Ab3C7z"
            )
            @PathParam("shortCode") @NotEmpty String shortCode) {

        return tinyUrlService.resolveUrl(shortCode)
                .map(originalUrl -> Response.status(Response.Status.FOUND)
                        .header("Location", originalUrl)
                        .build()
                );
    }

    /**
     * Retrieves information about a tiny URL asynchronously.
     *
     * @param shortCode The short code to retrieve information for
     * @return Uni with Response containing URL details
     */
    @GET
    @WithSession
    @Path("/api/urls/info/{shortCode}")
    @Operation(
            summary = "Get information about a tiny URL",
            description = "Retrieves details about a tiny URL without incrementing usage counters asynchronously"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "URL information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TinyUrlResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "URL not found",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    public Uni<Response> getTinyUrlInfo(
            @Parameter(description = "Short code of the URL", required = true)
            @PathParam("shortCode") @NotEmpty String shortCode) {

        return tinyUrlService.getTinyUrlInfo(shortCode)
                .map(response -> Response.ok(response).build());
    }

    /**
     * Deactivates a tiny URL asynchronously.
     *
     * @param shortCode The short code to deactivate
     * @return Uni with Response indicating success
     */
    @DELETE
    @Path("/api/urls/{shortCode}")
    @WithTransaction
    @Operation(
            summary = "Deactivate a tiny URL",
            description = "Deactivates a tiny URL making it unavailable for future use asynchronously"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "URL deactivated successfully"),
            @APIResponse(responseCode = "404", description = "URL not found")
    })
    public Uni<Response> deactivateTinyUrl(
            @Parameter(description = "Short code of the URL", required = true)
            @PathParam("shortCode") @NotEmpty String shortCode) {

        return tinyUrlService.deactivateTinyUrl(shortCode)
                .map(ignore -> Response.noContent().build());
    }

    /**
     * Updates the expiration time of a tiny URL asynchronously.
     *
     * @param shortCode The short code to update
     * @param expirationTime The new expiration time
     * @return Uni with Response containing updated URL details
     */
    @PUT
    @Path("/api/urls/{shortCode}/expiration")
    @WithTransaction
    @Operation(
            summary = "Update expiration time of a tiny URL",
            description = "Sets a new expiration time for the tiny URL asynchronously"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Expiration time updated successfully",
                    content = @Content(schema = @Schema(implementation = TinyUrlResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "URL not found",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    public Uni<Response> updateExpirationTime(
            @Parameter(description = "Short code of the URL", required = true)
            @PathParam("shortCode") @NotEmpty String shortCode,
            @Parameter(description = "New expiration time", required = true)
            @QueryParam("expirationTime") @NotNull LocalDateTime expirationTime) {

        return tinyUrlService.updateExpirationTime(shortCode, expirationTime)
                .map(response -> Response.ok(response).build());
    }

    /**
     * Updates the maximum usage limit of a tiny URL asynchronously.
     *
     * @param shortCode The short code to update
     * @param maxUsage The new maximum usage limit
     * @return Uni with Response containing updated URL details
     */
    @PUT
    @WithTransaction
    @Path("/api/urls/{shortCode}/max-usage")
    @Operation(
            summary = "Update maximum usage limit of a tiny URL",
            description = "Sets how many times the URL can be used asynchronously"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Maximum usage limit updated successfully",
                    content = @Content(schema = @Schema(implementation = TinyUrlResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "URL not found",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    public Uni<Response> updateMaxUsage(
            @Parameter(description = "Short code of the URL", required = true)
            @PathParam("shortCode") @NotEmpty String shortCode,
            @Parameter(description = "New maximum usage limit (0 for unlimited)", required = true)
            @QueryParam("maxUsage") @NotNull int maxUsage) {

        return tinyUrlService.updateMaxUsage(shortCode, maxUsage)
                .map(response -> Response.ok(response).build());
    }

    /**
     * Updates the maximum attempts allowed for a tiny URL asynchronously.
     *
     * @param shortCode The short code to update
     * @param maxAttempts The new maximum attempts limit
     * @return Uni with Response containing updated URL details
     */
    @PUT
    @WithTransaction
    @Path("/api/urls/{shortCode}/max-attempts")
    @Operation(
            summary = "Update maximum attempts limit of a tiny URL",
            description = "Sets how many times a user can attempt to access the URL asynchronously"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Maximum attempts limit updated successfully",
                    content = @Content(schema = @Schema(implementation = TinyUrlResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "URL not found",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    public Uni<Response> updateMaxAttempts(
            @Parameter(description = "Short code of the URL", required = true)
            @PathParam("shortCode") @NotEmpty String shortCode,
            @Parameter(description = "New maximum attempts limit (0 for unlimited)", required = true)
            @QueryParam("maxAttempts") @NotNull int maxAttempts) {

        return tinyUrlService.updateMaxAttempts(shortCode, maxAttempts)
                .map(response -> Response.ok(response).build());
    }
}
