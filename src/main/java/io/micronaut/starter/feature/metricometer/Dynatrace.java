package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Dynatrace extends MicrometerFeature {

    public Dynatrace(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-dynatrace";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".dynatrace.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".dynatrace.apiToken", "${DYNATRACE_DEVICE_API_TOKEN}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".dynatrace.uri", "${DYNATRACE_DEVICE_URI}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".dynatrace.deviceId", "${DYNATRACE_DEVICE_ID}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".dynatrace.step", "PT1M");
    }
}
