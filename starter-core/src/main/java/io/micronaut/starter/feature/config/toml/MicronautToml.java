/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.config.toml;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.JvmFeature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautToml implements JvmFeature {
    private static final String ARTIFACT_ID_MICRONAUT_TOML = "micronaut-toml";
    private static final Dependency DEPENDENCY_MICRONAUT_TOML_COMPILE = MicronautDependencyUtils.tomlDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_TOML)
            .compile()
            .build();

    @Override
    public String getName() {
        return "micronaut-toml";
    }

    @Override
    public String getDescription() {
        return "adds the Micronaut TOML Dependency";
    }

    @Override
    public String getTitle() {
        return "Micronaut TOML";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_TOML_COMPILE);
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
