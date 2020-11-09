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
package io.micronaut.starter.test.github.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

/**
 * @author Pavol Gressa
 * @since 2.2
 */
@Introspected
public class GitHubSecretsPublicKey {
    private final String keyId;
    private final String key;

    @JsonCreator
    public GitHubSecretsPublicKey(@JsonProperty("key_id") String keyId, @JsonProperty("key") String key) {
        this.keyId = keyId;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getKeyId() {
        return keyId;
    }
}

