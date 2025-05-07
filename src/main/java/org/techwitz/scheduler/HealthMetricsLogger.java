package org.techwitz.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.jboss.logging.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

@ApplicationScoped
public class HealthMetricsLogger {
    private static final Logger LOG = Logger.getLogger(HealthMetricsLogger.class);

    @Inject
    MetricRegistry metricRegistry;

    /**
     * Logs system health metrics every 5 minutes.
     */
    @Scheduled(every = "5m")
    void logHealthMetrics() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Memory metrics
        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax = memoryMXBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        double heapUtilization = (double) heapUsed / heapMax * 100;

        // CPU metrics
        double systemLoad = osMXBean.getSystemLoadAverage();

        // Thread metrics
        int threadCount = threadMXBean.getThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();

        LOG.infof("HEALTH_METRICS | Memory: %d/%d MB (%.2f%%) | CPU Load: %.2f | Threads: %d (Peak: %d)",
                heapUsed, heapMax, heapUtilization, systemLoad, threadCount, peakThreadCount);

        // Reset peak thread count to track periods
        threadMXBean.resetPeakThreadCount();
    }
}
