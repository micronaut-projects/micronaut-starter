package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class CloudWatch extends MicrometerFeature {

    public CloudWatch(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-cloudwatch";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".cloudwatch.enabled", true);
    }
}
