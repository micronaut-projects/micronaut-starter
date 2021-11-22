/*
 * Copyright 2017-2020 original authors
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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

@Singleton
public class SecurityJWT extends SecurityFeature {

    public static final int ORDER = 0;

    public SecurityJWT(SecurityAnnotations securityAnnotations) {
        super(securityAnnotations);
    }

    @Override
    public String getName() {
        return "security-jwt";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security JWT";
    }

    @Override
    public String getDescription() {
        return "Adds support for JWT (JSON Web Token) based Authentication";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.security.authentication", "bearer");
        generatorContext.getConfiguration().put("micronaut.security.token.jwt.signatures.secret.generator.secret", "\"${JWT_GENERATOR_SIGNATURE_SECRET:pleaseChangeThisSecretForANewOne}\"");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.security")
                .artifactId("micronaut-security-jwt")
                .compile());
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
