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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerArtifactId;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Requires(property = "micronaut.starter.feature.kafka.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Kafka extends EaseTestingFeature
        implements DefaultFeature, MessagingFeature, SharedTestResourceFeature, ContributingTestContainerArtifactId {
    public static final Dependency MICRONAUT_KAFKA = MicronautDependencyUtils
            .kafkaDependency()
            .artifactId("micronaut-kafka")
            .compile()
            .build();

    public static final String NAME = "kafka";
    private static final String TEST_CONTAINERS_ARTIFACT_ID_KAFKA = "kafka";

    public Kafka(TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
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
        return "Adds support for Kafka messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_KAFKA);
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.MESSAGING &&
                selectedFeatures.stream().noneMatch(MessagingFeature.class::isInstance);
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html";
    }

    @Override
    public String testContainersArtifactId() {
        return TEST_CONTAINERS_ARTIFACT_ID_KAFKA;
    }
}
