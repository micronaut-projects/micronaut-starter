package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Stackdriver extends MicrometerFeature {

    public Stackdriver(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-stackdriver";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".stackdriver.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".stackdriver.projectId", "${STACKDRIVER_PROJECT_ID}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".stackdriver.step", "PT1M");
    }
}
