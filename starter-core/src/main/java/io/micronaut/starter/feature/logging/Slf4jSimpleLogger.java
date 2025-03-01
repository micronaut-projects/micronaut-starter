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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.LoggingFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.ThirdPartyLibraryFeature;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.slf4j.simple.logger.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Slf4jSimpleLogger implements ThirdPartyLibraryFeature, LoggingFeature {
    public static final String NAME = "slf4j-simple-logger";

    private static final String SLF4J_SIMPLE_LOGGER_ARTIFACT_ID = "slf4j-simple-logger";

    private static final Dependency SLF4J_SIMPLE_LOGGER_DEPENDENCY = Dependency.builder()
            .lookupArtifactId(SLF4J_SIMPLE_LOGGER_ARTIFACT_ID)
            .runtime()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "SLF4J Simple Logger";
    }

    @Override
    public String getDescription() {
        return "Logger for applications in single-thread contexts.";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/GoodforGod/slf4j-simple-logger";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(SLF4J_SIMPLE_LOGGER_DEPENDENCY);
    }
}
