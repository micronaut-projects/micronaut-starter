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
package io.micronaut.starter.feature.sourcegen;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Singleton
public class SourcegenFeatureValidator implements FeatureValidator  {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof SourcegenJava)) {
            if (!supports(options.getLanguage())) {
                throw new IllegalArgumentException("sourcegen-generator is not supported in " + StringUtils.capitalize(options.getLanguage().getName()) + " applications");
            }
        }
    }

    public static List<Language> supportedLanguages() {
        return Stream.of(Language.values())
                .filter(SourcegenFeatureValidator::supports)
                .toList();
    }

    public static boolean supports(Language language) {
        return language != Language.GROOVY;
    }
}
