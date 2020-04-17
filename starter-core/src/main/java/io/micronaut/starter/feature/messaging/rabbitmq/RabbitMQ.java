package io.micronaut.starter.feature.messaging.rabbitmq;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;

import javax.inject.Singleton;

@Singleton
public class RabbitMQ implements Feature {

    @Override
    public String getName() {
        return "rabbitmq";
    }

    @Override
    public String getDescription() {
        return "Adds support for RabbitMQ in the application";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {

    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("rabbitmq.uri", "amqp://localhost:5672");
    }
}
