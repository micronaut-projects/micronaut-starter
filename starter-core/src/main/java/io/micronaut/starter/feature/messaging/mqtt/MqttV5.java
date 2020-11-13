package io.micronaut.starter.feature.messaging.mqtt;

import io.micronaut.starter.application.generator.GeneratorContext;

import javax.inject.Singleton;

@Singleton
public class MqttV5 extends AbstractMqttFeature implements MqttFeature {

    public static final String NAME = "mqttv5";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MQTT v5 Messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put ("mqtt.client.server-uri", "tcp://localhost:1883");
        generatorContext.getConfiguration().put ("mqtt.client.client-id", "${random.uuid}");
    }
}
