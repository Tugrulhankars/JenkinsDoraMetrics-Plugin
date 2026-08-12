package io.jenkins.plugins.dorametrics;

import hudson.model.Action;
import hudson.model.Item;
import hudson.model.Job;
import org.kohsuke.stapler.StaplerProxy;

/**
 * Represents the DORA Metrics action that appears on the Jenkins job page.
 * Implements StaplerProxy to enforce Item.READ permission before granting access to web requests.
 */
public class DoraMetricsAction implements Action, StaplerProxy {

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

    public Job<?, ?> getProject() {
        return project;
    }

    @Override
    public Object getTarget() {
        if (project != null) {
            project.checkPermission(Item.READ);
        }
        return this;
    }

    @Override
    public String getIconFileName() {
        return (project != null && project.hasPermission(Item.READ)) ? "graph.png" : null;
    }

    @Override
    public String getDisplayName() {
        return (project != null && project.hasPermission(Item.READ)) ? "DORA Metrics" : null;
    }

    @Override
    public String getUrlName() {
        return (project != null && project.hasPermission(Item.READ)) ? "dora-metrics" : null;
    }

    /**
     * Calculates the total number of successful deployments.
     *
     * @return The count of successful deployments for this project
     */
    public int getSuccessfulDeployments() {
        if (project != null) {
            project.checkPermission(Item.READ);
            return DoraStorage.get().getSuccessfulDeployments(project.getFullName());
        }
        return 0;
    }

    /**
     * Calculates the Change Failure Rate (CFR).
     * CFR is the percentage of deployments causing a failure in production.
     *
     * @return The change failure rate rounded to two decimal places
     */
    public double getChangeFailureRate() {
        if (project != null) {
            project.checkPermission(Item.READ);
            return Math.round(DoraStorage.get().getChangeFailureRate(project.getFullName()) * 100.0) / 100.0;
        }
        return 0.0;
    }

    /**
     * Calculates the Mean Lead Time for changes.
     *
     * @return The mean lead time in minutes rounded to two decimal places
     */
    public double getMeanLeadTime() {
        if (project != null) {
            project.checkPermission(Item.READ);
            return Math.round(DoraStorage.get().getMeanLeadTimeMinutes(project.getFullName()) * 100.0) / 100.0;
        }
        return 0.0;
    }

    /**
     * Calculates the Mean Time to Recovery (MTTR).
     *
     * @return The mean time to recovery in minutes rounded to two decimal places
     */
    public double getMeanTimeToRecovery() {
        if (project != null) {
            project.checkPermission(Item.READ);
            return Math.round(DoraStorage.get().getMeanTimeToRecoveryMinutes(project.getFullName()) * 100.0) / 100.0;
        }
        return 0.0;
    }
}
