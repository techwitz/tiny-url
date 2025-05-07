package org.techwitz.service;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.techwitz.domain.TinyUrl;
import org.techwitz.dto.TinyUrlRequest;
import org.techwitz.dto.TinyUrlResponse;
import org.techwitz.exception.MaxAttemptsExceededException;
import org.techwitz.exception.TinyUrlException;
import org.techwitz.exception.UrlExpiredException;
import org.techwitz.exception.UrlNotFoundException;
import org.techwitz.interceptor.Loggable;
import org.techwitz.repository.TinyUrlRepository;
import org.techwitz.util.ShortCodeGenerator;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Slf4j
@Loggable
@ApplicationScoped
public class TinyUrlService {

    @Inject
    TinyUrlRepository tinyUrlRepository;

    @Inject
    ShortCodeGenerator shortCodeGenerator;

    @ConfigProperty(name = "tiny.url.base.url")
    String baseUrl;

    @ConfigProperty(name = "tiny.url.code.length", defaultValue = "6")
    int shortCodeLength;

    /**
     * Creates a new tiny URL based on the provided request asynchronously.
     *
     * @param request The request containing original URL and configuration
     * @return Uni with Response containing the generated short URL
     */
    public Uni<TinyUrlResponse> createTinyUrl(TinyUrlRequest request) {
        validateUrl(request.getOriginalUrl());

        TinyUrl tinyUrl = new TinyUrl();
        tinyUrl.setOriginalUrl(request.getOriginalUrl());
        tinyUrl.setExpirationTime(request.getExpirationTime());
        tinyUrl.setOneTimeUse(request.isOneTimeUse());
        tinyUrl.setMaxUsage(request.getMaxUsage());
        tinyUrl.setMaxAttempts(request.getMaxAttempts());
        tinyUrl.setAttemptCount(0); // Explicitly set to ensure it's not null
        tinyUrl.setUsageCount(0);   // Explicitly set to ensure it's not null
        tinyUrl.setActive(true);
        tinyUrl.setCreatedAt(LocalDateTime.now());

        // Generate a unique short code
        return generateUniqueShortCodeAsync()
                .map(shortCode -> {
                    tinyUrl.setShortCode(shortCode);
                    log.debug("Generated short code: {}", shortCode);
                    return tinyUrl;
                })
                .flatMap(entity -> {
                    log.debug("Persisting tiny URL: {}", entity);
                    return tinyUrlRepository.persistAsync(entity);
                })
                .map(this::buildResponse);
    }

    /**
     * Retrieves the original URL for a given short code and updates usage statistics asynchronously.
     *
     * @param shortCode The short code to resolve
     * @return Uni with the original URL
     */
    public Uni<String> resolveUrl(String shortCode) {
        log.info("Resolving URL for short code: {}", shortCode);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(tinyUrl -> {
                    // Always increment the attempt count first
                    tinyUrl.incrementAttemptCount();
                    log.info(
                            "Incremented attempt count for URL with short code: {}, new count: {}",
                            shortCode, tinyUrl.getAttemptCount());

                    // Check if max attempts exceeded
                    if (tinyUrl.getMaxAttempts() > 0 && tinyUrl.getAttemptCount() > tinyUrl.getMaxAttempts()) {
                        log.warn(
                                "Maximum attempts exceeded for URL with short code: {}, attempts: {}, max: {}",
                                shortCode, tinyUrl.getAttemptCount(), tinyUrl.getMaxAttempts());
                        throw new MaxAttemptsExceededException("Maximum number of attempts exceeded for this URL");
                    }

                    if (tinyUrl.isExpired()) {
                        log.warn("URL with short code {} has expired or reached its usage limit", shortCode);
                        throw new UrlExpiredException("The tiny URL has expired or reached its usage limit");
                    }

                    // If we got here, the URL is valid and can be used
                    tinyUrl.incrementUsageCount();
                    log.info(
                            "Incremented usage count for URL with short code: {}, new count: {}",
                            shortCode, tinyUrl.getUsageCount());
                    return tinyUrl;
                })
                .flatMap(tinyUrlRepository::persistAsync)
                .map(tinyUrl -> {
                    String originalUrl = tinyUrl.getOriginalUrl();
                    log.info("Resolved URL with short code: {} to original URL: {}", shortCode, originalUrl);
                    return originalUrl;
                });
    }

    private Uni<TinyUrl> findTinyUrlByShortCodeAsync(String shortCode) {
        log.debug("Finding tiny URL by short code: {}", shortCode);
        return tinyUrlRepository.findByShortCodeAsync(shortCode)
                .onItem().ifNull().failWith(() ->
                                                    new UrlNotFoundException("Tiny URL not found for code: " + shortCode));
    }

    /**
     * Retrieves information about a tiny URL without incrementing its usage count asynchronously.
     *
     * @param shortCode The short code to retrieve information for
     * @return Uni with Response containing URL details
     */
    @CacheResult(cacheName = "tiny-url-info")
    public Uni<TinyUrlResponse> getTinyUrlInfo(String shortCode) {
        log.info("Retrieving tiny URL info for short code: {}", shortCode);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(this::buildResponse);
    }

    private TinyUrlResponse buildResponse(TinyUrl tinyUrl) {
        TinyUrlResponse response = new TinyUrlResponse();
        response.setOriginalUrl(tinyUrl.getOriginalUrl());
        response.setShortUrl(baseUrl + tinyUrl.getShortCode());
        response.setExpirationTime(tinyUrl.getExpirationTime());
        response.setOneTimeUse(tinyUrl.isOneTimeUse());
        response.setMaxUsage(tinyUrl.getMaxUsage());
        response.setUsageCount(tinyUrl.getUsageCount());
        response.setMaxAttempts(tinyUrl.getMaxAttempts());
        response.setAttemptCount(tinyUrl.getAttemptCount());
        return response;
    }

    /**
     * Deactivates a tiny URL asynchronously.
     *
     * @param shortCode The short code to deactivate
     * @return Uni<Void> representing completion
     */
    public Uni<Void> deactivateTinyUrl(String shortCode) {
        log.info("Deactivating tiny URL with short code: {}", shortCode);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(tinyUrl -> {
                    tinyUrl.setActive(false);
                    log.info("Deactivated tiny URL with short code: {}", shortCode);
                    return tinyUrl;
                })
                .flatMap(tinyUrlRepository::persistAsync)
                .replaceWithVoid();
    }

    /**
     * Updates the expiration time of a tiny URL asynchronously.
     *
     * @param shortCode      The short code to update
     * @param expirationTime The new expiration time
     * @return Uni with Response containing the updated URL details
     */
    public Uni<TinyUrlResponse> updateExpirationTime(String shortCode, LocalDateTime expirationTime) {
        log.info("Updating expiration time for short code: {} to: {}", shortCode, expirationTime);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(tinyUrl -> {
                    tinyUrl.setExpirationTime(expirationTime);
                    log.info("Updated expiration time for short code: {} to: {}", shortCode, expirationTime);
                    return tinyUrl;
                })
                .flatMap(tinyUrlRepository::persistAsync)
                .map(this::buildResponse);
    }

    // Private helper methods

    /**
     * Updates the maximum usage limit of a tiny URL asynchronously.
     *
     * @param shortCode The short code to update
     * @param maxUsage  The new maximum usage limit
     * @return Uni with Response containing the updated URL details
     */
    public Uni<TinyUrlResponse> updateMaxUsage(String shortCode, int maxUsage) {
        log.info("Updating maximum usage limit for short code: {} to: {}", shortCode, maxUsage);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(tinyUrl -> {
                    tinyUrl.setMaxUsage(maxUsage);
                    log.info("Updated maximum usage limit for short code: {} to: {}", shortCode, maxUsage);
                    return tinyUrl;
                })
                .flatMap(tinyUrlRepository::persistAsync)
                .map(this::buildResponse);
    }

    /**
     * Updates the maximum attempts allowed for a tiny URL asynchronously.
     *
     * @param shortCode   The short code to update
     * @param maxAttempts The new maximum attempts limit
     * @return Uni with Response containing the updated URL details
     */
    public Uni<TinyUrlResponse> updateMaxAttempts(String shortCode, int maxAttempts) {
        log.info("Updating maximum attempts for short code: {} to: {}", shortCode, maxAttempts);
        return findTinyUrlByShortCodeAsync(shortCode)
                .map(tinyUrl -> {
                    tinyUrl.setMaxAttempts(maxAttempts);
                    log.info("Updated maximum attempts for short code: {} to: {}", shortCode, maxAttempts);
                    return tinyUrl;
                })
                .flatMap(tinyUrlRepository::persistAsync)
                .map(this::buildResponse);
    }

    private Uni<String> generateUniqueShortCodeAsync() {
        return generateUniqueCodeWithRetry(0);
    }

    private Uni<String> generateUniqueCodeWithRetry(int attempts) {
        if (attempts > 10) {
            return Uni.createFrom().failure(
                    new TinyUrlException("Failed to generate unique code after 10 attempts")
            );
        }

        String shortCode = shortCodeGenerator.generate(shortCodeLength);
        log.debug("Generated candidate code: {} (attempt {})", shortCode, attempts);

        return tinyUrlRepository.findByShortCodeAsync(shortCode)
                .onItem().transform(existing -> {
                    return existing == null ? shortCode : null;
                })
                .onItem().ifNull().switchTo(() -> {
                    // Existing code found, retry with new code
                    return generateUniqueCodeWithRetry(attempts + 1);
                });
    }

    private void validateUrl(String url) {
        try {
            new URI(url);
        } catch (URISyntaxException e) {
            throw new TinyUrlException("Invalid URL format: " + url);
        }
    }
}
