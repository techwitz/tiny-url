package org.techwitz.job;

import org.techwitz.repository.TinyUrlRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CleanupJob {

    private static final Logger LOG = Logger.getLogger(CleanupJob.class);

    @Inject
    TinyUrlRepository tinyUrlRepository;

    /**
     * Scheduled job to clean up expired tiny URLs asynchronously.
     * Runs daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    void cleanupExpiredUrls() {
        LOG.info("Starting cleanup job for expired tiny URLs");

        tinyUrlRepository.deleteExpiredUrlsAsync()
                .subscribe().with(
                        deletedCount -> LOG.info("Cleanup job completed. Deleted " + deletedCount + " expired tiny URLs"),
                        error -> LOG.error("Error during cleanup job", error)
                );
    }
}
