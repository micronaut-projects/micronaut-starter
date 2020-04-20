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
package io.micronaut.starter.feature.messaging.rabbitmq;

import io.micronaut.starter.Options;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.messaging.Platform;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class RabbitMQ implements DefaultFeature {

    @Override
    public String getName() {
        return "rabbitmq";
    }

    @Override
    public String getTitle() {
        return "RabbitMQ Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for RabbitMQ in the application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("rabbitmq.uri", "amqp://localhost:5672");
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        Optional<Platform> platform = options.get("platform", Platform.class);
        return applicationType == ApplicationType.MESSAGING && platform.isPresent() && platform.get() == Platform.RABBITMQ;
    }
}
