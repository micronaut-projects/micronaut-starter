package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Humio extends MicrometerFeature {

    public Humio(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-humio";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".humio.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".humio.step", "PT1M");
    }
}
