package org.techwitz.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request object for creating a tiny URL")
public class TinyUrlRequest {

    @Schema(description = "The original URL to be shortened", required = true, examples = "https://example.com/very/long/url/path?param=value")
    private String originalUrl;

    @Schema(description = "Optional expiration time for the URL", examples = "2025-12-31T23:59:59")
    private LocalDateTime expirationTime;

    @Schema(description = "Whether the URL should be usable only once", examples = "false")
    private boolean oneTimeUse;

    @Schema(description = "Maximum number of times the URL can be used (0 for unlimited)", examples = "5")
    private int maxUsage;

    @Schema(description = "Maximum number of access attempts allowed (0 for unlimited)", examples = "10")
    private int maxAttempts;
}
