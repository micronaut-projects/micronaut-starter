package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Core implements Feature {

    @Override
    public String getName() {
        return "micrometer";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("micronaut.metrics.enabled", true);
    }
}
