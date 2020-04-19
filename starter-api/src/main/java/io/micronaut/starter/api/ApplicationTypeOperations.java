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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Get;

import java.util.List;

/**
 * Operations on application types.
 *
 * @author graemerocher
 * @since 1.0.0
 *
 */
public interface ApplicationTypeOperations {
    /**
     * List the application types.
     * @param request The request
     * @return The types
     */
    @Get("/application-types")
    List<ApplicationTypeDTO> list(HttpRequest<?> request);

    /**
     * Get a specific application type.
     * @param type The type
     * @param request The request
     * @return The type
     */
    @Get("/application-types/{type}")
    ApplicationTypeDTO getType(ApplicationTypes type, HttpRequest<?> request);

    /**
     * List the type features.
     * @param type The features
     * @return The features
     */
    @Get("/application-types/{type}/features")
    List<FeatureDTO> features(ApplicationTypes type);
}
