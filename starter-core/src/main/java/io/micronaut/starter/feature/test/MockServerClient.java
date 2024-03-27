/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerArtifactId;
import jakarta.inject.Singleton;

@Singleton
public class MockServerClient implements Feature, ContributingTestContainerArtifactId {

    public static final String NAME = "mockserver-client-java";
    public static final String ARTIFACT_ID_MOCKSERVER_CLIENT_JAVA = "mockserver-client-java";

    public static final Dependency DEPENDENCY_MOCKSERVER_CLIENT_JAVA = Dependency.builder()
            .lookupArtifactId(ARTIFACT_ID_MOCKSERVER_CLIENT_JAVA)
            .test()
            .build();
    public static final String TEST_CONTAINERS_ARTIFACT_ID_MOCKSERVER = "mockserver";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Java Client for Mockserver";
    }

    @Override
    public String getDescription() {
        return "Provides capability to connect to a MockServer";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://www.mock-server.com/mock_server/mockserver_clients.html#java-mockserver-client";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MOCKSERVER_CLIENT_JAVA);
    }

    @Override
    public String testContainersArtifactId() {
        return TEST_CONTAINERS_ARTIFACT_ID_MOCKSERVER;
    }
}
