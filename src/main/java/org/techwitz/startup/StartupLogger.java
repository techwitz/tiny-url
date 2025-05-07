package org.techwitz.startup;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ApplicationScoped
public class StartupLogger {
    private static final Logger LOG = Logger.getLogger(StartupLogger.class);

    @ConfigProperty(name = "quarkus.application.name", defaultValue = "tiny-url-service")
    String applicationName;

    @ConfigProperty(name = "quarkus.application.version", defaultValue = "unknown")
    String applicationVersion;

    @ConfigProperty(name = "quarkus.profile")
    Optional<String> profile;

    public void onStart(@Observes StartupEvent event) {
        LOG.info("-------------------------------------------------------------");
        LOG.infof("Application: %s (v%s) starting at %s",
                applicationName,
                applicationVersion,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        LOG.infof("Active profile: %s", profile.orElse("default"));

        // Log host information
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            LOG.infof("Server running on: %s (%s)",
                    localHost.getHostName(),
                    localHost.getHostAddress());
        } catch (UnknownHostException e) {
            LOG.warn("Could not determine hostname", e);
        }

        // Log JVM information
        Runtime runtime = Runtime.getRuntime();
        LOG.infof("JVM: %s (v%s)",
                System.getProperty("java.vm.name"),
                System.getProperty("java.version"));
        LOG.infof("Memory: Max: %d MB, Total: %d MB, Free: %d MB",
                runtime.maxMemory() / (1024 * 1024),
                runtime.totalMemory() / (1024 * 1024),
                runtime.freeMemory() / (1024 * 1024));
        LOG.infof("Processors: %d", runtime.availableProcessors());

        LOG.info("Application startup complete");
        LOG.info("-------------------------------------------------------------");
    }
}
