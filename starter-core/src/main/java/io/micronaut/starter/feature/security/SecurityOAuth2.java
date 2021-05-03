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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import javax.inject.Singleton;

@Singleton
public class SecurityOAuth2 implements Feature, MicronautServerDependent {

    public static final int ORDER = SecurityJWT.ORDER + 10;
    private final SecurityAnnotations securityAnnotations;

    public SecurityOAuth2(SecurityAnnotations securityAnnotations) {
        this.securityAnnotations = securityAnnotations;
    }

    @NonNull
    @Override
    public String getName() {
        return "security-oauth2";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security OAuth 2.0";
    }

    @Override
    public String getDescription() {
        return "Adds support for authentication with OAuth 2.0 providers";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(SecurityAnnotations.class)) {
            featureContext.addFeature(securityAnnotations);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.security.authentication", "cookie");
        generatorContext.getConfiguration().put("micronaut.security.oauth2.clients.default.client-id", "${OAUTH_CLIENT_ID}");
        generatorContext.getConfiguration().put("micronaut.security.oauth2.clients.default.client-secret", "${OAUTH_CLIENT_SECRET}");
        if (generatorContext.isFeaturePresent(SecurityJWT.class)) {
            generatorContext.getConfiguration().put("micronaut.security.oauth2.clients.default.openid.issuer", "${OAUTH_ISSUER}");
        }
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.security")
                .artifactId("micronaut-security-oauth2")
                .compile());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.SECURITY;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#oauth";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
