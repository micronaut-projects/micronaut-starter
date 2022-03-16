/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class Kafka implements DefaultFeature, MessagingFeature {

    public static final String NAME = "kafka";

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
        return "Adds support for Kafka messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("kafka.bootstrap.servers", "localhost:9092");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.kafka")
                .artifactId("micronaut-kafka")
                .compile());
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.MESSAGING &&
                selectedFeatures.stream().noneMatch(feature -> feature instanceof MessagingFeature);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html";
    }
}
