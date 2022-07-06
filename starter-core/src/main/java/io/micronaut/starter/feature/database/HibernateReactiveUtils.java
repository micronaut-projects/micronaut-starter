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
import io.micronaut.starter.feature.Feature;

import java.util.Set;

public final class HibernateReactiveUtils {

    private HibernateReactiveUtils() {
    }

    public static boolean isThereADatabaseDriverFeatureCompatibleWithHibernateReactive(@NonNull Set<Feature> features) {
        return features
                .stream()
                .filter(DatabaseDriverFeature.class::isInstance)
                .map(it -> (DatabaseDriverFeature) it)
                .anyMatch(HibernateReactiveUtils::supportsHibernateReactive);
    }

    public static boolean supportsHibernateReactive(@NonNull DatabaseDriverFeature f) {
        return f.getHibernateReactiveJavaClientDependency().isPresent();
    }
}
