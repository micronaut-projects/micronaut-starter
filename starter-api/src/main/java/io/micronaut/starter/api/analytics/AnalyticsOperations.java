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
package io.micronaut.starter.api.analytics;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.starter.analytics.Generated;

import java.util.concurrent.CompletableFuture;

/**
 * Interface to implement to provide analytics.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface AnalyticsOperations {

    /**
     * Report analytics.
     * @param generated The generated data
     * @return A future
     */
    CompletableFuture<HttpStatus> applicationGenerated(@NonNull Generated generated);
}
