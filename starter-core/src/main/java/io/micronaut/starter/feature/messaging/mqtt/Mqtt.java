package io.micronaut.starter.feature.messaging.mqtt;

import io.micronaut.starter.feature.FeatureContext;

import javax.inject.Singleton;

@Singleton
public class Mqtt extends AbstractMqttFeature {

    public static final String NAME = "mqtt";

    private final MqttV5 mqttv5;

    public Mqtt(MqttV5 mqttv5) {
        this.mqttv5 = mqttv5;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MQTT Messaging";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MqttFeature.class)) {
            featureContext.addFeature(mqttv5);
        }
    }

}
