/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.opensearch;

import jakarta.inject.Singleton;

@Singleton
public class OpenSearchHttpClient5 implements OpenSearchFeature {

    public static final String NAME = "opensearch-httpclient5";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenSearch HttpClient 5";
    }

    @Override
    public String getDescription() {
        return "Adds support for OpenSearch using Apache HttpClient 5 Transport";
    }
}
