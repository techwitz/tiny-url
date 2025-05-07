package org.techwitz.docs;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Path("/api/docs")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Tag(name = "API Documentation", description = "Documentation endpoints for the Tiny URL service")
public class ApiDocumentation {

    /**
     * Provides information about the OpenAI principles followed by this API.
     */
    @GET
    @Path("/principles")
    @Operation(
            summary = "Get API principles",
            description = "Returns the OpenAI principles followed by this API"
    )
    @APIResponse(
            responseCode = "200",
            description = "The principles followed by this API",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Map.class)
            )
    )
    public Map<String, Object> getPrinciples() {
        return Map.of(
                "name", "Tiny URL API Principles",
                "version", "1.0.0",
                "principles", Map.of(
                        "security", "We prioritize security in our design, including input validation, secure random generation for URLs, and rate limiting to prevent abuse.",
                        "privacy", "We respect user privacy by not collecting unnecessary personal information and by providing options for time-limited and usage-limited URLs.",
                        "transparency", "We provide clear documentation about how our API works and what data we collect.",
                        "robustness", "Our system is designed to be resilient, scalable, and maintainable, following best practices like clean architecture and SOLID principles.",
                        "accountability", "We track usage statistics to ensure service quality and to detect and prevent abuse.",
                        "fairness", "We provide equal access to our API for all users, with appropriate rate limiting to ensure fair distribution of resources."
                ),
                "additionalResources", Map.of(
                        "apiDocs", "/openapi",
                        "swaggerUi", "/swagger-ui",
                        "healthCheck", "/health",
                        "metrics", "/metrics"
                )
        );
    }

    /**
     * Provides usage guidelines for the API.
     */
    @GET
    @Path("/guidelines")
    @Operation(
            summary = "Get API usage guidelines",
            description = "Returns guidelines for using the API responsibly"
    )
    @APIResponse(
            responseCode = "200",
            description = "API usage guidelines",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Map.class)
            )
    )
    public Map<String, Object> getGuidelines() {
        return Map.of(
                "name", "Tiny URL API Usage Guidelines",
                "version", "1.0.0",
                "guidelines", Map.of(
                        "rateLimiting", "Please respect our rate limits to ensure fair usage for all users.",
                        "contentPolicy", "Do not use our service to shorten URLs to illegal or harmful content.",
                        "security", "Keep your API credentials secure and do not share them with others.",
                        "privacy", "Do not use our service to collect personal information without proper consent.",
                        "maintenance", "We may occasionally need to perform maintenance. Please check our status page for announcements."
                ),
                "contactInformation", Map.of(
                        "email", "support@example.com",
                        "website", "https://example.com/support",
                        "status", "https://status.example.com"
                )
        );
    }
}
