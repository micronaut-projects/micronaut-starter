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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class SecurityJWT extends SecurityFeature implements SecurityAuthenticationModeProvider {

    public static final String NAME = "security-jwt";

    public static final int ORDER = 0;

    public SecurityJWT(SecurityProcessor securityProcessor) {
        super(securityProcessor);
    }

    @Override
    public String getName() {
        return NAME;
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
        generatorContext.getConfiguration().put(PROPERTY_MICRONAUT_SECURITY_AUTHENTICATION, getSecurityAuthenticationMode().toString());
        Optional<SecurityAuthenticationMode> securityAuthenticationModeOptional = SecurityAuthenticationModeUtils.resolveSecurityAuthenticationMode(generatorContext);
        if (securityAuthenticationModeOptional.isPresent() &&
                (securityAuthenticationModeOptional.get() == SecurityAuthenticationMode.BEARER || securityAuthenticationModeOptional.get() == SecurityAuthenticationMode.COOKIE)
        ) {
            generatorContext.getConfiguration().put("micronaut.security.token.jwt.signatures.secret.generator.secret", "${JWT_GENERATOR_SIGNATURE_SECRET:pleaseChangeThisSecretForANewOne}");
        }
        generatorContext.addDependency(MicronautDependencyUtils.securityDependency()
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

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.BEARER;
    }
}
