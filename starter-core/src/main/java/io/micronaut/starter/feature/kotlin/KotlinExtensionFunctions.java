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
package io.micronaut.starter.feature.kotlin;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class KotlinExtensionFunctions implements Feature, LanguageSpecificFeature {

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof KotlinExtensionFunctions;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("Kotlin Extension Functions feature only supports Kotlin");
                }
            });
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "kotlin-extension-functions";
    }

    @Override
    public String getTitle() {
        return "Kotlin Extension Functions";
    }

    @Override
    public String getDescription() {
        return "Variety of convenience functions to make using micronaut with kotlin more user-friendly";
    }

    public String getCategory() {
        return Category.LANGUAGES;
    }

    @Override
    public Language getRequiredLanguage() {
        return Language.KOTLIN;
    }
}
