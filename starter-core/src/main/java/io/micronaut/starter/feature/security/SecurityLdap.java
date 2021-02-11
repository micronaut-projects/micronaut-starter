/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.security;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import javax.inject.Singleton;

@Singleton
public class SecurityLdap implements Feature, MicronautServerDependent {

    private final SecurityAnnotations securityAnnotations;

    public SecurityLdap(SecurityAnnotations securityAnnotations) {
        this.securityAnnotations = securityAnnotations;
    }

    @NonNull
    @Override
    public String getName() {
        return "security-ldap";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security LDAP";
    }

    @Override
    public String getDescription() {
        return "Adds support for authentication with LDAP servers";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(SecurityAnnotations.class)) {
            featureContext.addFeature(securityAnnotations);
        }
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
        return "https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#ldap";
    }
}
