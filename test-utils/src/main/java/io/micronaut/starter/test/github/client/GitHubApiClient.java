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

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client(id = GitHubApiClient.SERVICE_URL)
@Header(name = "User-Agent", value = "https://micronaut.io/launch/")
@Header(name = "Accept", value = GitHubApiClient.GITHUB_V3_TYPE)
public interface GitHubApiClient {
    String SERVICE_URL = "https://api.github.com";
    String GITHUB_V3_TYPE = "application/vnd.github.v3+json";

    @Post(value = "/user/repos", single = true)
    GitHubRepository createRepository(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @Body GitHubRepository gitHubRepository);

    @Delete(value = "/repos/{owner}/{repo}")
    void deleteRepository(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo);

    @Get(value = "/repos/{owner}/{repo}", single = true)
    GitHubRepository getRepository(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo);

    @Get(value = "/user", single = true)
    GitHubUser getUser(@Header(HttpHeaders.AUTHORIZATION) String oauthToken);

    @Put(value = "/repos/{owner}/{repo}/actions/secrets/{secretName}")
    void createSecret(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable String secretName,
            @Body GitHubSecret secret);

    @Get(value = "/repos/{owner}/{repo}/actions/secrets/public-key")
    GitHubSecretsPublicKey getSecretPublicKey(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo);

    @Get(value = "/repos/{owner}/{repo}/actions/runs")
    GitHubWorkflowRuns listWorkflows(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo);

    @Get(value = "/repos/{owner}/{repo}/actions/runs/{runId}")
    GitHubWorkflowRun getWorkflowRun(
            @Header(HttpHeaders.AUTHORIZATION) String oauthToken,
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable Long runId);
}
