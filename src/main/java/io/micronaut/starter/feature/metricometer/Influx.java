package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Influx extends MicrometerFeature {

    public Influx(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-influx";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".influx.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".influx.step", "PT1M");
    }
}
