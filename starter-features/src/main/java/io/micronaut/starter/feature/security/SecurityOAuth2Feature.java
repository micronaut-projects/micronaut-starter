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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;

public abstract class SecurityOAuth2Feature implements Feature {
    private final SecurityOAuth2 securityOAuth2;
    private final SecurityJWT securityJWT;

    public SecurityOAuth2Feature(SecurityOAuth2 securityOAuth2,
                                 @Nullable SecurityJWT securityJWT) {
        this.securityOAuth2 = securityOAuth2;
        this.securityJWT = securityJWT;
    }

    public SecurityOAuth2Feature(SecurityOAuth2 securityOAuth2) {
        this(securityOAuth2, null);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (securityJWT != null && !featureContext.isPresent(SecurityJWT.class)) {
            featureContext.addFeature(securityJWT);
        }
        if (!featureContext.isPresent(SecurityOAuth2.class)) {
            featureContext.addFeature(securityOAuth2);
        }
    }
}
