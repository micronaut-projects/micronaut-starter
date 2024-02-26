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
package io.micronaut.starter.feature.security;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Singleton
public class SecurityProcessor implements Feature, MicronautServerDependent {

    public static final String NAME = "security-processor";

    private static final String ARTIFACT_ID_MICRONAUT_SECURITY_ANNOTATIONS = "micronaut-security-processor";
    private static final String PROPERTY_MICRONAUT_SECURITY_VERSION = "micronaut.security.version";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(),
                MicronautDependencyUtils.GROUP_ID_MICRONAUT_SECURITY,
                ARTIFACT_ID_MICRONAUT_SECURITY_ANNOTATIONS,
                PROPERTY_MICRONAUT_SECURITY_VERSION));
    }
}
