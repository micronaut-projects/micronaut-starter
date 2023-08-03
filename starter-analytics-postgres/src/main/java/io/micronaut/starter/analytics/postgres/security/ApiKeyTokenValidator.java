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

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

/**
 * Validates token {@code X-API-KEY} and retrieves claims.
 */
@Singleton
class ApiKeyTokenValidator implements TokenValidator<HttpRequest<?>> {

    private final ApiKeyRepository apiKeyRepository;

    ApiKeyTokenValidator(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public Publisher<Authentication> validateToken(String token, HttpRequest<?> request) {
        return apiKeyRepository.findByApiKey(token)
                .map(Authentication::build)
                .map(Publishers::just)
                .orElseGet(Publishers::empty);
    }
}
