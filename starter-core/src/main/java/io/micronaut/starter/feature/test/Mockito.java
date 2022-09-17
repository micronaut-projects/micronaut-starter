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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class Mockito implements MockingFeature {
    public static final String GROUP_ID_MOCKITO = "org.mockito";
    public static final String ARTIFACT_ID_MOCKITO_CORE = "mockito-core";
    public static final Dependency DEPENDENCY_MOCKITO_CORE = Dependency.builder()
            .groupId(GROUP_ID_MOCKITO)
            .artifactId(ARTIFACT_ID_MOCKITO_CORE)
            .test()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "mockito";
    }

    @Override
    public String getTitle() {
        return "Mockito Framework";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Mockito test mocking framework for JUnit";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://site.mockito.org";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MOCKITO_CORE);
    }
}
