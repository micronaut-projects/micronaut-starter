package io.micronaut.starter.feature.messaging.mqtt;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;

public abstract class AbstractMqttFeature implements Feature {

    @Override
    public String getDescription() {
        return "Adds support for MQTT in the application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html";
    }
}
