/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.config;

import io.micronaut.projectgen.core.feature.ConfigurationFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.Options;

import java.util.Set;

/**
 * Marker interface for the {@link ConfigurationFeature} which is a {@link DefaultFeature}.
 */
public interface DefaultConfigurationFeature extends ConfigurationFeature, DefaultFeature {

    @Override
    default boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(ConfigurationFeature.class::isInstance);
    }
}
