/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.starter.feature.ThirdPartyLibraryFeature;
import jakarta.inject.Singleton;

@Singleton
public class LiquibaseSlf4j implements ThirdPartyLibraryFeature, LoggingFeature {
    private static final String ARTIFACT_ID_LIQUIBASE_SLF_4_J = "liquibase-slf4j";

    @Override
    public String getName() {
        return "liquibase-slf4j";
    }

    @Override
    public String getTitle() {
        return "Liquibase SLF4J";
    }

    @Override
    public String getDescription() {
        return "An implementation of the Liquibase logger that delegates directly to SLF4J";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
    
    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId(ARTIFACT_ID_LIQUIBASE_SLF_4_J).runtime());
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/mattbertolini/liquibase-slf4j";
    }
}
