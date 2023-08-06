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
package io.micronaut.starter.feature.validation;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Singleton
public class RequiredLanguageFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        Map<Language, Set<String>> requiredLanguages = new HashMap<>();
        for (Feature feature: features) {
            if (feature instanceof LanguageSpecificFeature specificFeature) {
                Language lang = specificFeature.getRequiredLanguage();
                requiredLanguages.compute(lang, (key, value) -> {
                    if (value == null) {
                        value = new HashSet<>();
                    }
                    value.add(feature.getName());
                    return value;
                });
            }
        }

        Set<Language> languages = requiredLanguages.keySet();
        Iterator<Language> languageIterator = languages.iterator();

        if (languages.size() > 1) {
            Language first = languageIterator.next();
            Language second = languageIterator.next();
            throw new IllegalArgumentException("The selected features are incompatible. %s requires %s and %s requires %s".formatted(requiredLanguages.get(first), first, requiredLanguages.get(second), second));
        }

        Language requiredLanguage = languageIterator.hasNext() ? languageIterator.next() : null;

        if (options != null && requiredLanguage != null && requiredLanguage != options.getLanguage()) {
            throw new IllegalArgumentException("The selected features are incompatible. %s requires %s but %s was the selected language.".formatted(requiredLanguages.get(requiredLanguage), requiredLanguage, options.getLanguage()));
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }
}
