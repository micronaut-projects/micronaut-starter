package io.micronaut.starter.feature.metricometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class NewRelic extends MicrometerFeature {

    public NewRelic(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-new-relic";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".newrelic.enabled", true);
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".newrelic.apiKey", "${NEWRELIC_API_KEY}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".newrelic.accountId", "${NEWRELIC_ACCOUNT_ID}");
        commandContext.getConfiguration().put(EXPORT_PREFIX + ".newrelic.step", "PT1M");
    }
}
