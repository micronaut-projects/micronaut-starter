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

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import io.micronaut.starter.feature.other.HttpClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SecurityOAuth2 extends SecurityFeature implements SecurityAuthenticationModeProvider {

    public static final String NAME = "security-oauth2";
    public static final int ORDER = SecurityJWT.ORDER + 10;

    private final HttpClient httpClient;

    @Inject
    public SecurityOAuth2(SecurityProcessor securityProcessor, HttpClient httpClient) {
        super(securityProcessor);
        this.httpClient = httpClient;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(HttpClientFeature.class, httpClient);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Security OAuth 2.0";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for authentication with OAuth 2.0 providers";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(PROPERTY_MICRONAUT_SECURITY_AUTHENTICATION,
                SecurityAuthenticationModeUtils.resolveSecurityAuthenticationMode(generatorContext)
                        .orElseGet(this::getSecurityAuthenticationMode).toString());

        SecurityOAuth2Configuration oAuth2Config = securityOAuth2Configuration(generatorContext);

        ApplicationConfiguration devConfig = generatorContext.getConfiguration(Environment.DEVELOPMENT, ApplicationConfiguration.devConfig());
        devConfig.put("micronaut.security.oauth2.clients.default.client-id", oAuth2Config.getClientId());
        devConfig.put("micronaut.security.oauth2.clients.default.client-secret", oAuth2Config.getClientSecret());
        if (generatorContext.isFeaturePresent(SecurityJWT.class)) {
            devConfig.put("micronaut.security.oauth2.clients.default.openid.issuer", oAuth2Config.getIssuer());
        }
        generatorContext.addDependency(MicronautDependencyUtils.securityDependency()
                .artifactId("micronaut-security-oauth2")
                .compile());
    }

    @NonNull
    private SecurityOAuth2Configuration securityOAuth2Configuration(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getFeatures()
                .getFeatures()
                .stream()
                .filter(SecurityOAuth2Configuration.class::isInstance)
                .map(SecurityOAuth2Configuration.class::cast)
                .findFirst()
                .orElseGet(() -> new SecurityOAuth2Configuration() { });
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#oauth";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.COOKIE;
    }
}
