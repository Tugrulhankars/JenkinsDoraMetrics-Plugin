package io.jenkins.plugins.dorametrics;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Item;
import hudson.model.Job;
import java.util.Collection;
import java.util.Collections;
import jenkins.model.TransientActionFactory;

/**
 * Factory class that attaches the DORA Metrics action to Jenkins jobs.
 * Uses TransientActionFactory to dynamically attach actions to all job types.
 */
@Extension
public class DoraActionFactory extends TransientActionFactory<Job> {

    @Override
    public Class<Job> type() {
        return Job.class;
    }

    @Override
    public Collection<? extends Action> createFor(Job target) {
        if (target == null || !target.hasPermission(Item.READ)) {
            return Collections.emptyList();
        }
        return Collections.singleton(new DoraMetricsAction(target));
    }
}
