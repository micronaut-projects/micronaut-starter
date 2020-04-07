package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Atlas extends MicrometerFeature {

    public Atlas(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-atlas";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.uri", "http://localhost:7101/api/v1/publish");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.step", "PT1M");
    }
}
