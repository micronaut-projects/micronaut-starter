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
package io.micronaut.starter.feature.lang.java;

import io.micronaut.starter.Options;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Java implements LanguageFeature {

    private final List<JavaApplicationFeature> applicationFeatures;

    public Java(List<JavaApplicationFeature> applicationFeatures,
                Junit junit) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public String getTitle() {
        return "Java Support";
    }

    @Override
    public String getDescription() {
        return getTitle();
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.hasApplicationFeature()) {
            applicationFeatures.stream()
                    .filter(f -> f.supports(featureContext.getCommand()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, List<Feature> selectedFeatures) {
        return options.getLanguage() == Language.JAVA;
    }
}
