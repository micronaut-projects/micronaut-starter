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

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.feature.Feature;

/**
 * Represents an application feature.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
public class FeatureDTO {
    private final String name;
    private final String description;

    /**
     * Default constructor.
     * @param feature The feature
     */
    public FeatureDTO(Feature feature) {
        this.name = feature.getName();
        this.description = feature.getDescription();
    }

    /**
     * Default constructor.
     * @param name The name
     * @param description The description
     */
    @Creator
    public FeatureDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return The name of the feature
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of the feature
     */
    public String getDescription() {
        return description;
    }
}
