package io.jenkins.plugins.dorametrics;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.TransientProjectActionFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Factory class that attaches the DORA Metrics action to Jenkins jobs.
 * By extending TransientProjectActionFactory, we dynamically attach our action to all projects.
 */
@Extension
public class DoraActionFactory extends TransientProjectActionFactory {


    /**
     * Specifies the type of jobs this action should be attached to.
     * 
     * @return Job.class to ensure the action runs on all job types (Pipeline, Freestyle, etc.)
     */
    public Class<? extends Job> type() {
        return Job.class;
    }



    /**
     * Creates and attaches the DoraMetricsAction to the specified target project.
     * 
     * @param target The Jenkins project
     * @return A collection containing our custom action
     */
    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        return Collections.singleton(new DoraMetricsAction(target));
    }
}