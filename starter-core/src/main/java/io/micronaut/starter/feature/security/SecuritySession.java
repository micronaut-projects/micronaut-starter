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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.HttpSession;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.security.SecurityAuthenticationModeProvider.PROPERTY_MICRONAUT_SECURITY_AUTHENTICATION;

@Singleton
public class SecuritySession extends SecurityFeature implements SecurityAuthenticationModeProvider {

    public static final int ORDER = SecurityOAuth2.ORDER + 10;

    public SecuritySession(SecurityAnnotations securityAnnotations) {
        super(securityAnnotations);
    }

    @Override
    public String getName() {
        return "security-session";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security Session";
    }

    @Override
    public String getDescription() {
        return "Adds support for Session based Authentication";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.exclude(HttpSession.class::isInstance);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(PROPERTY_MICRONAUT_SECURITY_AUTHENTICATION, getSecurityAuthenticationMode().toString());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.security")
                .artifactId("micronaut-security-session")
                .compile());
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#session";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.SESSION;
    }
}
