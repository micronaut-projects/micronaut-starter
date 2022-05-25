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
package io.micronaut.starter.feature.aws;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.security.SecurityAuthenticationMode;
import io.micronaut.starter.feature.security.SecurityAuthenticationModeProvider;
import io.micronaut.starter.feature.security.SecurityJWT;
import io.micronaut.starter.feature.security.SecurityOAuth2;
import io.micronaut.starter.feature.security.SecurityOAuth2Configuration;
import io.micronaut.starter.feature.security.SecurityOAuth2Feature;
import jakarta.inject.Singleton;

@Singleton
public class AmazonCognito extends SecurityOAuth2Feature implements AwsFeature, SecurityOAuth2Configuration, SecurityAuthenticationModeProvider {
    public static final String NAME = "amazon-cognito";

    public AmazonCognito(SecurityOAuth2 securityOAuth2, SecurityJWT securityJWT) {
        super(securityOAuth2, securityJWT);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Amazon Cognito";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Applies Micronaut Security OAuth 2.0 and provides configuration. Can be used in combination with '" + Cdk.NAME + "' to generate a Cognito User Pool with a OAuth 2.0 application.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    @NonNull
    public String getCategory() {
        return Category.SECURITY;
    }

    @Override
    @NonNull
    public String getIssuer() {
        return "https://cognito-idp.${COGNITO_REGION:us-east-1}.amazonaws.com/${COGNITO_POOL_ID:ZZZ}/";
    }

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.IDTOKEN;
    }
}
