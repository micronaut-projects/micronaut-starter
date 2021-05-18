/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.api;

import io.micronaut.starter.application.ApplicationType;

import java.util.List;
import java.util.Locale;

/**
 * API to expose information about features.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface FeatureOperations {
    /**
     * List all the available features.
     * @param locale The locale
     * @return The available features
     */
    List<FeatureDTO> getAllFeatures(Locale locale);

    /**
     * A list of features applicable to the given application type.
     * @param locale The locale
     * @param type The type
     * @return The features
     */
    List<FeatureDTO> getFeatures(Locale locale, ApplicationType type);
}
