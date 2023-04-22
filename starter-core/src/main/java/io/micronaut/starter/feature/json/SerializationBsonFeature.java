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
package io.micronaut.starter.feature.json;

import jakarta.inject.Singleton;

@Singleton
public class SerializationBsonFeature implements SerializationFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SERDE_BSON = "micronaut-serde-bson";

    @Override
    public String getName() {
        return "serialization-bson";
    }

    @Override
    public String getDescription() {
        return "Adds support using Micronaut Serialization with BSON";
    }

    @Override
    public String getTitle() {
        return "Micronaut Serialization BSON";
    }

    @Override
    public String getModule() {
        return "bson";
    }

}
