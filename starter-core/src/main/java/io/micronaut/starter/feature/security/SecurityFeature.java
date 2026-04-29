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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;

public abstract class SecurityFeature implements Feature, MicronautServerDependent {

    private final SecurityProcessor securityProcessor;

    public SecurityFeature(SecurityProcessor securityProcessor) {
        this.securityProcessor = securityProcessor;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(SecurityProcessor.class)) {
            featureContext.addFeature(securityProcessor);
        }
    }

    @Override
    public String getCategory() {
        return Category.SECURITY;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
