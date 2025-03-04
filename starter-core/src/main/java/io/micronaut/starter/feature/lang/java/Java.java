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
package io.micronaut.starter.feature.lang.java;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.JavaApplicationFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Requires(property = "micronaut.starter.feature.java.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Java implements LanguageFeature {

    protected final List<JavaApplicationFeature> applicationFeatures;

    public Java(List<JavaApplicationFeature> applicationFeatures) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    @NonNull
    public String getName() {
        return "java";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        processSelectedFeatures(featureContext, feature -> true);
    }

    protected void processSelectedFeatures(FeatureContext featureContext, Predicate<Feature> featureFilter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            ApplicationType type = featureContext.getOptions() instanceof MicronautOptions mnOptions ? mnOptions.applicationType() : null;
            applicationFeatures.stream()
                    .filter(featureFilter)
                    .filter(f -> f.supports(MicronautOptions.builder().applicationType(type).build()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options.language() == Language.JAVA;
    }
}
