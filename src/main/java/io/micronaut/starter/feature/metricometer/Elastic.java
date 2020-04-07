package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Elastic extends MicrometerFeature {

    public Elastic(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-elastic";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".elastic.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".elastic.step", "PT1M");
    }
}
