package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Ganglia extends MicrometerFeature {

    public Ganglia(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-ganglia";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".ganglia.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".ganglia.protocolVersion", 3.1);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".ganglia.step", "PT1M");
    }
}
