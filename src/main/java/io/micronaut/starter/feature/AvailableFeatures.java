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
package io.micronaut.starter.feature;

import java.util.*;
import java.util.stream.Stream;

public abstract class AvailableFeatures implements Iterable<String> {

    private final Map<String, Feature> features;

    public AvailableFeatures(List<Feature> features) {
        this.features = new LinkedHashMap<>(features.size());
        for (Feature feature: features) {
            this.features.put(feature.getName(), feature);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return getFeatures()
                .map(Feature::getName)
                .iterator();
    }

    public Optional<Feature> findFeature(String name) {
        return findFeature(name, false);
    }

    public Optional<Feature> findFeature(String name, boolean ignoreVisibility) {
        Feature feature = features.get(name);
        if (feature != null) {
            if (ignoreVisibility || feature.isVisible()) {
                return Optional.of(feature);
            }
        }
        return Optional.empty();
    }

    public Stream<Feature> getFeatures() {
        Stream<Feature> stream = features.values().stream();
        return stream.filter(Feature::isVisible);
    }

    public Stream<Feature> getAllFeatures() {
        return features.values().stream();
    }

}
