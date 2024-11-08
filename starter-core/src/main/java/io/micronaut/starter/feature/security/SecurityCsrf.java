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
package io.micronaut.starter.feature.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

@Singleton
public class SecurityCsrf extends SecurityFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SECURITY_CSRF = "micronaut-security-csrf";
    private static final Dependency DEPENDENCY_MICRONAUT_SECURITY_CSRF = MicronautDependencyUtils.securityDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_SECURITY_CSRF)
            .compile()
            .build();

    public SecurityCsrf(SecurityAnnotations securityAnnotations) {
        super(securityAnnotations);
    }

    @NonNull
    @Override
    public String getName() {
        return "security-csrf";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security CSRF";
    }

    @Override
    public String getDescription() {
        return "Adds Cross Site Request Forgery (CSRF) mitigation APIs";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_SECURITY_CSRF);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#csrf";
    }
}
