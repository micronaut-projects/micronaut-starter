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

import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.ApplicationType;

import java.util.List;

/**
 * A default feature is one that should be applied to a
 * project conditionally without being explicitly selected.
 * If a feature must be chosen by the user in order to be applied,
 * then the feature is not a default feature.
 *
 * @author James Kleeh
 * @since 2.0.0
 */
public interface DefaultFeature extends Feature {

    /**
     * Determines if the feature should be applied to the project.
     *
     * @param applicationType  The application type
     * @param options          The options
     * @param selectedFeatures The features manually selected by the user
     * @return True if the feature should apply
     */
    boolean shouldApply(ApplicationType applicationType,
                        Options options,
                        List<Feature> selectedFeatures);
}
