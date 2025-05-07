package org.techwitz.interceptor;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ApplicationScoped
public class LoggerInitializer {
    @Inject
    @ConfigProperty(name = "quarkus.log.file.path", defaultValue = "logs/app.log")
    String logFilePath;

    public void onStart(@Observes StartupEvent event) {
        try {
            Path currentWorkingDir = Paths.get("").toAbsolutePath();
            log.debug("Application working directory: {}", "" + currentWorkingDir);
            log.info("Initializing logging at {}....", logFilePath);
            Path logPath = Paths.get(logFilePath);
            Path logDir = logPath.getParent();
            log.debug("Log file path: {}", logPath);

            if (logDir != null && !Files.exists(logDir)) {
                Files.createDirectories(logDir);
                log.info("Created log directory: " + logDir);
            }

            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
                log.info("Created log file: " + logPath);
            }
        } catch (IOException e) {
            log.error("Failed to create log file directory", e);
        }
    }
}
