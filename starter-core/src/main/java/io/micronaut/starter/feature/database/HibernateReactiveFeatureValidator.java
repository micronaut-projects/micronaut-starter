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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class HibernateReactiveFeatureValidator implements FeatureValidator {
    private final String errorMsg;

    public HibernateReactiveFeatureValidator(List<DatabaseDriverFeature> databaseDriverFeatures) {
        StringBuilder errorMsg = new StringBuilder("Hibernate Reactive requires ");
        List<String> hibernateReactiveDatabaseDriverFeatures = databaseDriverFeatures.stream()
                .filter(HibernateReactiveFeatureValidator::supportsHibernateReactive)
                .map(Feature::getName)
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < hibernateReactiveDatabaseDriverFeatures.size(); i++) {
            errorMsg.append(hibernateReactiveDatabaseDriverFeatures.get(i));
            if (i <= (hibernateReactiveDatabaseDriverFeatures.size() - 2)) {
                errorMsg.append(", ");
            }
            if (i == (hibernateReactiveDatabaseDriverFeatures.size() - 2)) {
                errorMsg.append("or ");
            }
        }
        this.errorMsg = errorMsg.toString();
    }

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(HibernateReactiveFeature.class::isInstance) && !isThereADatabaseDriverFeatureCompatibleWithHibernateReactive(features)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(HibernateReactiveFeature.class::isInstance)
                && options.getJavaVersion().majorVersion() < JdkVersion.JDK_11.majorVersion()) {
            throw new IllegalArgumentException("Hibernate Reactive requires at least JDK 11");
        }
    }

    public static boolean isThereADatabaseDriverFeatureCompatibleWithHibernateReactive(@NonNull Set<Feature> features) {
        return features
                .stream()
                .filter(DatabaseDriverFeature.class::isInstance)
                .map(it -> (DatabaseDriverFeature) it)
                .anyMatch(HibernateReactiveFeatureValidator::supportsHibernateReactive);
    }

    public static boolean supportsHibernateReactive(@NonNull DatabaseDriverFeature f) {
        return f.getHibernateReactiveJavaClientDependency().isPresent();
    }
}
