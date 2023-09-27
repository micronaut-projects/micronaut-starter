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
package io.micronaut.starter.feature.logging;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CommunityFeature;
import jakarta.inject.Singleton;

@Singleton
public class Slf4jSimpleLogger implements CommunityFeature {
    public static final String NAME = "goodforgod-slf4j-simple-logger";

    private static final String SLF4J_SIMPLE_LOGGER_ARTIFACT_ID = "slf4j-simple-logger";

    private static final Dependency SLF4J_SIMPLE_LOGGER_DEPENDENCY = Dependency.builder()
            .lookupArtifactId(SLF4J_SIMPLE_LOGGER_ARTIFACT_ID)
            .compile()
            .build();

    @Override
    public String getCommunityFeatureTitle() {
        return "Slf4j Simple Logger";
    }

    @Override
    public String getCommunityFeatureName() {
        return "slf4j-simple-logger";
    }

    @Override
    public String getCommunityContributor() {
        return "goodforgod";
    }

    @Override
    public String getDescription() {
        return "Logger for applications in single-thread contexts.";
    }

    @Override
    public String getCategory() {
        return Category.LOGGING;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/GoodforGod/slf4j-simple-logger";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(SLF4J_SIMPLE_LOGGER_DEPENDENCY);
    }
}
