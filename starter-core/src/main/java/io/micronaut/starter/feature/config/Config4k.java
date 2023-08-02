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
package io.micronaut.starter.feature.config;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.feature.KotlinSpecificFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.Config4kTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.function.Function;

@Singleton
public class Config4k implements ConfigurationFeature, KotlinSpecificFeature {

    private static final String EXTENSION = "conf";

    @NonNull
    @Override
    public String getName() {
        return "config4k";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Config4k - Config for Kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof Config4k;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("config4k feature only supports Kotlin");
                }
            });
        }
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Define configuration with config4k, a typesafe configuration format for Kotlin based on HOCON";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return config -> new Config4kTemplate(config.getFullPath(EXTENSION), config);
    }

}
