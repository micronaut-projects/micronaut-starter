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
package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.messaging.jms.SQS;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerArtifactId;
import jakarta.inject.Singleton;

/**
 * Adds support for <a href="https://localstack.cloud/">LocalStack</a>.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 3.7.1
 */
@Singleton
public class LocalStack implements Feature, ContributingTestContainerArtifactId {

    public static final String ARTIFACT_ID_LOCALSTACK = "localstack";
    private final TestContainers testContainers;

    public LocalStack(TestContainers testContainers) {
        this.testContainers = testContainers;
    }

    @Override
    @NonNull
    public String getName() {
        return "localstack";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getTitle() {
        return "LocalStack";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "A fully functional local cloud stack to develop and test your cloud and serverless apps offline, integrated via Testcontainers";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.testcontainers.org/modules/localstack/";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestContainers.class)) {
            featureContext.addFeature(testContainers);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        // SQS pulls this in transitively so this is not required
        if (!generatorContext.isFeaturePresent(SQS.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.amazonaws")
                    .artifactId("aws-java-sdk-core")
                    .test());
        }
    }

    @Override
    public String testContainersArtifactId() {
        return ARTIFACT_ID_LOCALSTACK;
    }
}
