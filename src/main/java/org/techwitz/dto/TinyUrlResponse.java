package org.techwitz.dto;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Schema(description = "Response object containing tiny URL details")
public class TinyUrlResponse {

    @Schema(description = "The original URL that was shortened")
    private String originalUrl;

    @Schema(description = "The generated short URL")
    private String shortUrl;

    @Schema(description = "Expiration time for the URL (if set)")
    private LocalDateTime expirationTime;

    @Schema(description = "Whether the URL is usable only once")
    private boolean oneTimeUse;

    @Schema(description = "Maximum number of times the URL can be used (0 for unlimited)")
    private int maxUsage;

    @Schema(description = "Current usage count")
    private int usageCount;

    @Schema(description = "Maximum number of access attempts allowed (0 for unlimited)")
    private int maxAttempts;

    @Schema(description = "Current attempt count")
    private int attemptCount;

    // Getters and setters...

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }
}

