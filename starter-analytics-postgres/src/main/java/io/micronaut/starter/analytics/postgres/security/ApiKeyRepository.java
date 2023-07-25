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
package io.micronaut.starter.analytics.postgres.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

/**
 * Repository for API keys.
 */
@Singleton
class ApiKeyRepository {

    private final Map<String, String> keys;

    ApiKeyRepository(List<ApiKeyConfiguration> keys) {
        this.keys = keys.stream().collect(toMap(ApiKeyConfiguration::getKey, ApiKeyConfiguration::getName));
    }

    /**
     * Finds the principal name that corresponds to the given API key.
     *
     * @param apiKey The API key.
     * @return An optional principal name.
     */
    @NonNull
    public Optional<String> findByApiKey(@NonNull @NotBlank String apiKey) {
        return Optional.ofNullable(keys.get(apiKey));
    }
}
