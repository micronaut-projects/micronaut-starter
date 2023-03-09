/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.client.github.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

/**
 * @author Pavol Gressa
 * @since 2.2
 */
@Introspected
public class AccessToken {

    private final String tokenType;
    private final String scope;
    private final String accessToken;

    @JsonCreator
    public AccessToken(
            @NonNull @JsonProperty("token_type") String tokenType,
            @NonNull @JsonProperty("scope") String scope,
            @NonNull @JsonProperty("access_token") String accessToken) {
        this.tokenType = tokenType;
        this.scope = scope;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
