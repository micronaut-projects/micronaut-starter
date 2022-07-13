/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.messaging.mqtt;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import jakarta.inject.Singleton;

@Singleton
public class Mqtt extends AbstractMqttFeature implements SharedTestResourceFeature {

    public static final String NAME = "mqtt";

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
        super.apply(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.mqtt")
                .artifactId("micronaut-mqttv5")
                .compile());
    }
}
