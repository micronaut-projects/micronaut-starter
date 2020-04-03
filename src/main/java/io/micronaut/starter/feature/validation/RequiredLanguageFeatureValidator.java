package io.micronaut.starter.feature.validation;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class RequiredLanguageFeatureValidator implements FeatureValidator {

    @Override
    public void validate(Language language, List<Feature> features) {
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

        if (language != null && requiredLanguage != null && requiredLanguage != language) {
            throw new IllegalArgumentException(String.format("The selected features are incompatible. %s requires %s but %s was the selected language.", requiredLanguages.get(requiredLanguage), requiredLanguage, language));
        }
    }
}
