package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Wavefront extends MicrometerFeature {

    public Wavefront(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-wavefront";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".wavefront.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".wavefront.apiToken", "${WAVEFRONT_API_TOKEN}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".wavefront.step", "PT1M");
    }
}
