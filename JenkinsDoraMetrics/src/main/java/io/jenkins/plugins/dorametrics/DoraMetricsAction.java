package io.jenkins.plugins.dorametrics;

import hudson.model.Action;
import hudson.model.Job;

/**
 * Represents the DORA Metrics action that appears on the Jenkins job page.
 * This class provides the necessary data to the UI to display the metrics.
 */
public class DoraMetricsAction implements Action {

    // Reference to the Jenkins job (project) this action belongs to
    private final Job<?, ?> project;

    /**
     * Constructor to initialize the action with the specific project.
     * 
     * @param project The Jenkins job associated with this action
     */
    public DoraMetricsAction(Job<?, ?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() { return "graph.png"; }

    @Override
    public String getDisplayName() { return "DORA Metrics"; }

    @Override
    public String getUrlName() { return "dora-metrics"; }

    /**
     * Calculates the total number of successful deployments.
     * 
     * @return The count of successful deployments for this project
     */
    public int getSuccessfulDeployments() {
        return DoraStorage.get().getSuccessfulDeployments(project.getFullName());
    }

    /**
     * Calculates the Change Failure Rate (CFR).
     * CFR is the percentage of deployments causing a failure in production.
     * 
     * @return The change failure rate rounded to two decimal places
     */
    public double getChangeFailureRate() {
        return Math.round(DoraStorage.get().getChangeFailureRate(project.getFullName()) * 100.0) / 100.0;
    }

    /**
     * Calculates the Mean Lead Time for changes.
     * 
     * @return The mean lead time in minutes rounded to two decimal places
     */
    public double getMeanLeadTime() {
        return Math.round(DoraStorage.get().getMeanLeadTimeMinutes(project.getFullName()) * 100.0) / 100.0;
    }

    /**
     * Calculates the Mean Time to Recovery (MTTR).
     * 
     * @return The mean time to recovery in minutes rounded to two decimal places
     */
    public double getMeanTimeToRecovery() {
        return Math.round(DoraStorage.get().getMeanTimeToRecoveryMinutes(project.getFullName()) * 100.0) / 100.0;
    }
}