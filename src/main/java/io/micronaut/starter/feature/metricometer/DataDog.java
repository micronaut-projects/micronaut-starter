package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class DataDog extends MicrometerFeature {

    public DataDog(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-datadog";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.apiKey", "${DATADOG_APIKEY}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.step", "PT1M");
    }
}
