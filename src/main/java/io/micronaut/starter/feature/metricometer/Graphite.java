package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Graphite extends MicrometerFeature {

    public Graphite(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-graphite";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".graphite.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".graphite.host", "localhost");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".graphite.port", 2004);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".graphite.step", "PT1M");
    }
}
