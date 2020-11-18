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
package io.micronaut.starter.client.github.oauth;

import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

/**
 * GitHub Oauth operations.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
public interface GitHubOAuthOperations {

    @Post(value = "/access_token")
    AccessToken accessToken(@QueryValue("client_id") String clientId, @QueryValue("client_secret") String clientSecret, @QueryValue String code, @QueryValue String state);

}
