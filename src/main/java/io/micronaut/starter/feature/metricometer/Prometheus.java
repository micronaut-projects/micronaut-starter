package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Prometheus extends MicrometerFeature {

    public Prometheus(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-prometheus";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".prometheus.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".prometheus.descriptions", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".prometheus.step", "PT1M");
    }
}
