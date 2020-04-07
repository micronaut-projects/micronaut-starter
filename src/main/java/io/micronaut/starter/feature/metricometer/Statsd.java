package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class Statsd extends MicrometerFeature {

    public Statsd(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-statsd";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".statsd.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".statsd.flavor", "datadog");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".statsd.host", "localhost");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".statsd.port", 8125);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".statsd.step", "PT1M");
    }
}
