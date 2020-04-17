package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.starter.Options;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.messaging.Platform;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class Kafka implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Options options, List<Feature> selectedFeatures) {
        Optional<Platform> platform = options.get("platform", Platform.class);
        return micronautCommand == MicronautCommand.CREATE_MESSAGING && platform.isPresent() && platform.get() == Platform.kafka;
    }

    @Override
    public String getName() {
        return "kafka";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("kafka.bootstrap.servers", "localhost:9092");
    }
}
