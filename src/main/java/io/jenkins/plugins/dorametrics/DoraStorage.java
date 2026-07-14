package io.jenkins.plugins.dorametrics;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Describable;
import jenkins.model.Jenkins;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistent storage for DORA Metrics.
 * This class stores deployment and failure data for each Jenkins project and calculates the metrics.
 */
@Extension
public class DoraStorage extends Descriptor<DoraStorage> implements Describable<DoraStorage> {

    /**
     * Inner class representing the raw metrics data collected for a single project.
     */
    public static class ProjectMetrics {
        public int totalDeployments = 0;
        public int failedDeployments = 0;
        public long totalLeadTime = 0;
        public long totalRecoveryTime = 0;
        public int recoveryCount = 0;
        public long lastFailureTimestamp = 0;
    }

    // In-memory map to hold metrics for all projects, keyed by project name
    private Map<String, ProjectMetrics> projectMetricsMap = new HashMap<>();

    public DoraStorage() {
        super(DoraStorage.class);
        // Load existing data from disk on startup
        load();
    }

    /**
     * Retrieves the singleton instance of DoraStorage from Jenkins.
     */
    public static DoraStorage get() {
        return Jenkins.get().getDescriptorByType(DoraStorage.class);
    }

    /**
     * Helper method to get or initialize metrics for a given project.
     */
    private synchronized ProjectMetrics getOrCreateMetrics(String projectName) {
        return projectMetricsMap.computeIfAbsent(projectName, k -> new ProjectMetrics());
    }

    /**
     * Records a build execution and updates the accumulated metrics.
     * 
     * @param projectName Name of the Jenkins project
     * @param isSuccess True if the build succeeded, false otherwise
     * @param duration Execution time of the build in milliseconds
     * @param timestamp Completion timestamp of the build
     */
    public synchronized void recordBuild(String projectName, boolean isSuccess, long duration, long timestamp) {
        ProjectMetrics metrics = getOrCreateMetrics(projectName);
        metrics.totalDeployments++;

        if (isSuccess) {
            metrics.totalLeadTime += duration;
            // If the previous state was failed, calculate recovery time
            if (metrics.lastFailureTimestamp > 0) {
                long recoveryTime = timestamp - metrics.lastFailureTimestamp;
                metrics.totalRecoveryTime += recoveryTime;
                metrics.recoveryCount++;
                // Reset failure timestamp since it is recovered now
                metrics.lastFailureTimestamp = 0;
            }
        } else {
            metrics.failedDeployments++;
            // Record the failure timestamp if this is the first failure in a row
            if (metrics.lastFailureTimestamp == 0) {
                metrics.lastFailureTimestamp = timestamp;
            }
        }
        // Save the updated state to disk
        save();
    }


    /**
     * Returns the count of successful deployments.
     */
    public synchronized int getSuccessfulDeployments(String projectName) {
        ProjectMetrics m = projectMetricsMap.get(projectName);
        return m != null ? (m.totalDeployments - m.failedDeployments) : 0;
    }

    /**
     * Returns the Change Failure Rate as a percentage.
     */
    public synchronized double getChangeFailureRate(String projectName) {
        ProjectMetrics m = projectMetricsMap.get(projectName);
        if (m == null || m.totalDeployments == 0) return 0.0;
        return ((double) m.failedDeployments / m.totalDeployments) * 100.0;
    }

    /**
     * Returns the Mean Lead Time in minutes.
     */
    public synchronized double getMeanLeadTimeMinutes(String projectName) {
        ProjectMetrics m = projectMetricsMap.get(projectName);
        if (m == null || m.totalDeployments == 0) return 0.0;
        return (double) (m.totalLeadTime / m.totalDeployments) / (1000 * 60);
    }

    /**
     * Returns the Mean Time to Recovery (MTTR) in minutes.
     */
    public synchronized double getMeanTimeToRecoveryMinutes(String projectName) {
        ProjectMetrics m = projectMetricsMap.get(projectName);
        if (m == null || m.recoveryCount == 0) return 0.0;
        return (double) (m.totalRecoveryTime / m.recoveryCount) / (1000 * 60);
    }

    @Override
    public String getDisplayName() { return "DORA Storage Configuration"; }
}