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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class HibernateReactiveFeatureValidator implements FeatureValidator {

    private final String errorMsg;
    public HibernateReactiveFeatureValidator(List<DatabaseDriverFeature> databaseDriverFeatures) {
        String errorMsg = "Hibernate Reactive requires ";
        List<DatabaseDriverFeature> hibernateReactiveDatabaseDriverFeatures = databaseDriverFeatures.stream()
                .filter(it -> it.getHibernateReactiveJavaClientDependency().isPresent())
                .collect(Collectors.toList());
        for (int i = 0; i < hibernateReactiveDatabaseDriverFeatures.size(); i++) {

            errorMsg += hibernateReactiveDatabaseDriverFeatures.get(i).getName();
            if (i <= (hibernateReactiveDatabaseDriverFeatures.size() - 2)) {
                errorMsg += ", ";
            }
            if (i == (hibernateReactiveDatabaseDriverFeatures.size() - 2)) {
                errorMsg += "or ";
            }
        }
        this.errorMsg = errorMsg;
    }

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(HibernateReactiveFeature.class::isInstance) &&
                !HibernateReactiveUtils.isThereADatabaseDriverFeatureCompatibleWithHibernateReactive(features)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        // Do nothing
    }
}
