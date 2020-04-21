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
package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.messaging.rabbitmq.RabbitMQ;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Kafka implements DefaultFeature {

    public static final String NAME = "kafka";

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        if (applicationType == ApplicationType.MESSAGING) {
            return selectedFeatures.stream().noneMatch(feature ->
                    feature.getName().equals(RabbitMQ.NAME)
            );
        } else {
            return selectedFeatures.stream().anyMatch(feature ->
                    feature.getName().equals(getName())
            );
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Kafka Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("kafka.bootstrap.servers", "localhost:9092");
    }
}
