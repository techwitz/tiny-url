package org.techwitz.config;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.cache.CacheManager;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.quarkus.redis.datasource.value.SetArgs;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RedisConfig {
    private static final Logger LOG = Logger.getLogger(RedisConfig.class);

    @Inject
    RedisDataSource redisDataSource;

    @Inject
    CacheManager cacheManager;

    @ConfigProperty(name = "tiny.url.redis.default.ttl", defaultValue = "3600")
    int defaultTtlSeconds;

    private ValueCommands<String, Object> objectValueCommands;

    @PostConstruct
    void init() {
        this.objectValueCommands = redisDataSource.value(Object.class);
        LOG.info("Redis configuration initialized successfully");
    }


    /**
     * Caches a value with the default TTL.
     *
     * @param key The cache key
     * @param value The value to cache
     */
    public void cache(String key, String value) {
        var setArgs = new SetArgs().ex(defaultTtlSeconds);
        objectValueCommands.set(key, value, setArgs);
    }

    /**
     * Caches a value with a specific TTL.
     *
     * @param key The cache key
     * @param value The value to cache
     * @param ttlSeconds The time-to-live in seconds
     */
    public void cache(String key, Object value, int ttlSeconds) {
        var setArgs = new SetArgs().ex(ttlSeconds);
        objectValueCommands.set(key, value, setArgs);
    }

    /**
     * Retrieves a cached value.
     *
     * @param key The cache key
     * @return The cached value, or null if not found
     */
    public Object getCached(String key) {
        return objectValueCommands.get(key);
    }

    /**
     * Removes a value from the cache.
     *
     * @param key The cache key to remove
     * @return true if the key was removed, false otherwise
     */
    public boolean invalidate(String key) {
        return objectValueCommands.getdel(key) != null;
    }
}