/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
