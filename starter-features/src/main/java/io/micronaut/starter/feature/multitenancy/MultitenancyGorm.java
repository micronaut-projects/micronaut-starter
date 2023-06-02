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
package io.micronaut.starter.feature.multitenancy;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.feature.GroovySpecificFeature;
import io.micronaut.starter.options.Language;

import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class MultitenancyGorm implements GroovySpecificFeature {

    private final Multitenancy multitenancy;

    public MultitenancyGorm(Multitenancy multitenancy) {
        this.multitenancy = multitenancy;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() == Language.GROOVY) {
            if (!featureContext.isPresent(Multitenancy.class)) {
                featureContext.addFeature(multitenancy);
            }
        } else {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof MultitenancyGorm;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("multi-tenancy-gorm feature only supports Groovy");
                }
            });
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.groovy")
                .artifactId("micronaut-multitenancy-gorm")
                .compile());
    }

    @NonNull
    @Override
    public String getName() {
        return "multi-tenancy-gorm";
    }

    @Override
    public String getTitle() {
        return "Multitenancy GORM";
    }

    @Override
    public String getDescription() {
        return "Integrates Micronaut multitenancy capabilities with GORM multitenancy features";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#multitenancyGorm";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://gorm.grails.org/latest/hibernate/manual/index.html#multiTenancy";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
