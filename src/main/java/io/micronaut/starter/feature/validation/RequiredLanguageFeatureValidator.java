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
package io.micronaut.starter.feature.validation;

import io.micronaut.starter.Options;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class RequiredLanguageFeatureValidator implements FeatureValidator {

    @Override
    public void validate(Options options, List<Feature> features) {
        Map<Language, Set<String>> requiredLanguages = new HashMap<>();
        for (Feature feature: features) {
            feature.getRequiredLanguage().ifPresent(lang -> {
                requiredLanguages.compute(lang, (key, value) -> {
                    if (value == null) {
                        value = new HashSet<>();
                    }
                    value.add(feature.getName());
                    return value;
                });
            });
        }

        Set<Language> languages = requiredLanguages.keySet();
        Iterator<Language> languageIterator = languages.iterator();

        if (languages.size() > 1) {
            Language first = languageIterator.next();
            Language second = languageIterator.next();
            throw new IllegalArgumentException(String.format("The selected features are incompatible. %s requires %s and %s requires %s", requiredLanguages.get(first), first, requiredLanguages.get(second), second));
        }

        Language requiredLanguage = languageIterator.hasNext() ? languageIterator.next() : null;

        if (options != null && requiredLanguage != null && requiredLanguage != options.getLanguage()) {
            throw new IllegalArgumentException(String.format("The selected features are incompatible. %s requires %s but %s was the selected language.", requiredLanguages.get(requiredLanguage), requiredLanguage, options.getLanguage()));
        }
    }
}
