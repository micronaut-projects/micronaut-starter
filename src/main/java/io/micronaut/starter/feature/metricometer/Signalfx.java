package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Signalfx extends MicrometerFeature {

    public Signalfx(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-signalfx";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".signalfx.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".signalfx.accessToken", "${SIGNALFX_API_TOKEN}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".signalfx.step", "PT1M");
    }
}
