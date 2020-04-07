package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class AppOptics extends MicrometerFeature {

    public AppOptics(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-appoptics";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".appoptics.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".appoptics.apiToken", "${APPOPTICS_API_TOKEN}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".appoptics.step", "PT1M");
    }
}
