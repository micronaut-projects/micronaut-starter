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
package io.micronaut.starter.api.create.github.client.v3;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.*;

/**
 * GitHub Oauth operations.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
public interface GitHubApiOperations {

    @Post(value = "/user/repos", single = true)
    GitHubRepository createRepository(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @Body GitHubRepository gitHubRepository);

    @Get(value = "/repos/{owner}/{repo}", single = true)
    GitHubRepository getRepository(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo);

    @Get(value = "/user", single = true)
    GitHubUser getUser(@Header(HttpHeaders.AUTHORIZATION) String oauthToken);

}
