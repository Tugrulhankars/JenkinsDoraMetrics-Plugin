package io.jenkins.plugins.dorametrics;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * A listener that triggers when a Jenkins build (run) completes.
 * It calculates and stores DORA metrics based on the build's result and duration.
 */
@Extension
public class DoraMetricsListener extends RunListener<Run<?, ?>> {

    /**
     * Called when a build completes. Collects necessary data and stores it.
     * 
     * @param run The build/run that just completed
     * @param listener The task listener for logging output
     */
    @Override
    public void onCompleted(Run<?, ?> run, TaskListener listener) {
        // The full name of the project in Jenkins
        String projectName = run.getParent().getFullName(); 
        boolean isSuccess = (run.getResult() == hudson.model.Result.SUCCESS);
        long duration = run.getDuration();
        long endTimestamp = System.currentTimeMillis();

        // Log to the Jenkins build console
        listener.getLogger().println("[DORA Metrics] Recording data for " + projectName + "...");

        // Store the metrics data
        DoraStorage.get().recordBuild(projectName, isSuccess, duration, endTimestamp);
    }
}