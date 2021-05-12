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

import io.micronaut.http.annotation.Get;
import io.micronaut.starter.application.ApplicationType;
import io.swagger.v3.oas.annotations.Parameter;


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
     * @param serverURL The server URL
     * @return The types
     */
    @Get("/application-types")
    ApplicationTypeList list(@Parameter(hidden = true) RequestInfo serverURL);

    /**
     * Get a specific application type.
     * @param type The type
     * @param serverURL The server URL
     * @return The type
     */
    @Get("/application-types/{type}")
    ApplicationTypeDTO getType(ApplicationType type, @Parameter(hidden = true) RequestInfo serverURL);

    /**
     * List the type features.
     * @param type The features
     * @param serverURL The server URL
     * @return The features
     */
    @Get("/application-types/{type}/features")
    FeatureList features(ApplicationType type, @Parameter(hidden = true) RequestInfo serverURL);
}
