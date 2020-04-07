package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Kairos extends MicrometerFeature {

    public Kairos(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-kairos";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".kairos.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".kairos.step", "PT1M");
    }
}
