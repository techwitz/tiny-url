package org.techwitz.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Tiny URL API",
                version = "1.0.0",
                description = "API for creating and managing shortened URLs with various constraints and features",
                termsOfService = "https://example.com/terms",
                contact = @Contact(
                        name = "API Support",
                        url = "https://example.com/support",
                        email = "support@example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "https://api.example.com", description = "Production"),
                @Server(url = "https://staging.example.com", description = "Staging"),
                @Server(url = "http://localhost:8080", description = "Development")
        }
)
@ApplicationScoped
public class OpenApiConfig {
    // Configuration class to define OpenAPI metadata
}
