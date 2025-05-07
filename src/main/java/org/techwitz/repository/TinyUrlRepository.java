package org.techwitz.repository;

import org.techwitz.domain.TinyUrl;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TinyUrlRepository implements PanacheRepository<TinyUrl> {

    /**
     * Finds a tiny URL by its short code asynchronously.
     *
     * @param shortCode The short code to search for
     * @return Uni containing the TinyUrl if found, or null if not found
     */
    public Uni<TinyUrl> findByShortCodeAsync(String shortCode) {
        return find("shortCode", shortCode).firstResult();
    }

    /**
     * Finds all expired tiny URLs asynchronously.
     *
     * @return Uni with List of expired tiny URLs
     */
    public Uni<List<TinyUrl>> findExpiredUrlsAsync() {
        LocalDateTime now = LocalDateTime.now();
        return list("expirationTime < ?1 AND expirationTime IS NOT NULL", now);
    }

    /**
     * Finds all inactive tiny URLs asynchronously.
     *
     * @return Uni with List of inactive tiny URLs
     */
    public Uni<List<TinyUrl>> findInactiveUrlsAsync() {
        return list("active = false");
    }

    /**
     * Deletes expired tiny URLs asynchronously.
     *
     * @return Uni with Number of records deleted
     */
    public Uni<Long> deleteExpiredUrlsAsync() {
        LocalDateTime now = LocalDateTime.now();
        return delete("expirationTime < ?1 AND expirationTime IS NOT NULL", now);
    }

    /**
     * Persists a tiny URL asynchronously.
     *
     * @param tinyUrl The tiny URL to persist
     * @return Uni with the persisted entity
     */
    public Uni<TinyUrl> persistAsync(TinyUrl tinyUrl) {
        return persistAndFlush(tinyUrl);
    }
}