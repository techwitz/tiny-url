package org.techwitz.domain;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tiny_urls")
public class TinyUrl extends PanacheEntity {

    @Column(nullable = false, name = "original_url")
    private String originalUrl;

    @Column(nullable = false, unique = true, name = "short_code")
    private String shortCode;

    @Column(nullable = true, name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(nullable = false, name = "one_time_use")
    private boolean oneTimeUse;

    @Column(nullable = false, name = "usage_count")
    private int usageCount;

    @Column(nullable = false, name = "max_usage")
    private int maxUsage;

    // Added field for maximum allowed attempts
    @Column(nullable = false, name = "max_attempts")
    private int maxAttempts;

    // Added field to track current attempts
    @ColumnDefault("0")
    @Column(nullable = false, name = "attempt_count")
    private int attemptCount;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "active")
    private boolean active;

    // Constructors, getters, and setters
    public TinyUrl() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
        this.usageCount = 0;
        this.attemptCount = 0;
        this.maxAttempts = 0; // Unlimited by default
    }

    public void incrementAttemptCount() {
        this.attemptCount++;
    }

    public boolean isExpired() {
        if (expirationTime != null && LocalDateTime.now().isAfter(expirationTime)) {
            return true;
        }

        if (oneTimeUse && usageCount >= 1) {
            return true;
        }

        if (maxUsage > 0 && usageCount >= maxUsage) {
            return true;
        }

        if (maxAttempts > 0 && attemptCount >= maxAttempts) {
            return true;
        }

        return !active;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }
}
