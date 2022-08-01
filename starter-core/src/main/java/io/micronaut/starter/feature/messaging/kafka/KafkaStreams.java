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
package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleFactoryGroovy;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleFactoryJava;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleFactoryKotlin;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleListenerGroovy;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleListenerJava;
import io.micronaut.starter.feature.messaging.kafka.templates.exampleListenerKotlin;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

@Singleton
public class KafkaStreams extends EaseTestingFeature implements MessagingFeature, SharedTestResourceFeature {

    private final Kafka kafka;

    public KafkaStreams(TestContainers testContainers, TestResources testResources, Kafka kafka) {
        super(testContainers, testResources);
        this.kafka = kafka;
    }

    @NonNull
    @Override
    public String getName() {
        return "kafka-streams";
    }

    @Override
    public String getTitle() {
        return "Kafka Streams";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka Streams";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Kafka.class)) {
            featureContext.addFeature(kafka);
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html#kafkaStream";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();

        String exampleListener = generatorContext.getSourcePath("/{packagePath}/ExampleListener");
        generatorContext.addTemplate("exampleListener", exampleListener,
            exampleListenerJava.template(project),
            exampleListenerKotlin.template(project),
            exampleListenerGroovy.template(project));

        String exampleFactory = generatorContext.getSourcePath("/{packagePath}/ExampleFactory");
        generatorContext.addTemplate("exampleFactory", exampleFactory,
            exampleFactoryJava.template(project),
            exampleFactoryKotlin.template(project),
            exampleFactoryGroovy.template(project));

        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.kafka")
                .artifactId("micronaut-kafka-streams")
                .compile());
    }
}
