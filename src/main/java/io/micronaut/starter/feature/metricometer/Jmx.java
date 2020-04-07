package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Jmx extends MicrometerFeature {

    public Jmx(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-jmx";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".jmx.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".jmx.step", "PT1M");
    }
}
