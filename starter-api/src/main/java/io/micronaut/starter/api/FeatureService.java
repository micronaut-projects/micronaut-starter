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
package io.micronaut.starter.api;

import io.micronaut.context.BeanLocator;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the {@link FeatureOperations} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Singleton
public class FeatureService implements FeatureOperations {

    private final List<Feature> features;
    private final BeanLocator beanLocator;

    /**
     * Default constructor.
     * @param features The features
     */
    public FeatureService(List<Feature> features, BeanLocator beanLocator) {
        this.features = features;
        this.beanLocator = beanLocator;
    }

    @Override
    public List<FeatureDTO> getAllFeatures() {
        return features.stream()
                .filter(Feature::isVisible)
                .map(FeatureDTO::new)
                .sorted(Comparator.comparing(FeatureDTO::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeatureDTO> getFeatures(ApplicationType type) {
        return beanLocator.getBean(AvailableFeatures.class, Qualifiers.byName(type.getName()))
                .getFeaturesStream()
                .map(FeatureDTO::new)
                .sorted(Comparator.comparing(FeatureDTO::getName))
                .collect(Collectors.toList());
    }
}
