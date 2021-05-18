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
package io.micronaut.starter.client.github.v3;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

/**
 * GitHub API v3 operations.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
@Client(id = GitHubApiClient.SERVICE_ID)
@Requires(property = "micronaut.http.services." + GitHubApiClient.SERVICE_ID)
@Header(name = "User-Agent", value = "https://micronaut.io/launch/")
@Header(name = "Accept", value = GitHubApiClient.GITHUB_V3_TYPE)
public interface GitHubApiClient extends GitHubApiOperations {
    String SERVICE_ID = "github-api-v3";
    String SERVICE_URL = "https://api.github.com";
    String GITHUB_V3_TYPE = "application/vnd.github.v3+json";

}
