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
package io.micronaut.starter.api.create.github;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.starter.api.StarterConfiguration;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.api.create.AbstractCreateController;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.client.github.oauth.AccessToken;
import io.micronaut.starter.client.github.oauth.GitHubOAuthClient;
import io.micronaut.starter.client.github.v3.GitHubApiClient;
import io.micronaut.starter.client.github.v3.GitHubRepository;
import io.micronaut.starter.client.github.v3.GitHubUser;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.util.GitHubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * GitHub create service.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
@Singleton
@Requires(beans = GitHubOAuthClient.class)
public class GitHubCreateService extends AbstractCreateController {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubCreateService.class);
    private static final String TOKEN_PREFIX = "token ";
    private static final String REPO_PREFIX = "generated";
    private static final String TMP_DIR = "/tmp";

    private final GitHubOAuthClient gitHubOAuthClient;
    private final GitHubApiClient gitHubApiClient;
    private final StarterConfiguration.GitHubConfiguration gitHubConfiguration;

    public GitHubCreateService(
            @NotNull ProjectGenerator projectGenerator,
            @NotNull ApplicationEventPublisher eventPublisher,
            @NotNull GitHubOAuthClient gitHubOAuthClient,
            @NotNull GitHubApiClient gitHubApiClient,
            @NotNull StarterConfiguration.GitHubConfiguration gitHubConfiguration) {
        super(projectGenerator, eventPublisher);
        this.gitHubOAuthClient = gitHubOAuthClient;
        this.gitHubApiClient = gitHubApiClient;
        this.gitHubConfiguration = gitHubConfiguration;
    }

    protected GitHubRepository creatApp(
            @NonNull ApplicationType type,
            @NonNull String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @NonNull String code,
            @NonNull String state,
            @Nullable String userAgent) {
        AccessToken accessToken = getGitHubAccessToken(code, state);
        String authToken = TOKEN_PREFIX + accessToken.getAccessToken();

        GitHubUser gitHubUser = getGitHubUser(authToken);

        GeneratorContext generatorContext = createProjectGeneratorContext(
                type, name, features, buildTool, testFramework, lang, javaVersion, userAgent);

        String repoName = generatorContext.getProject().getName();
        String repoDescription = String.format("Micronaut %s Application", generatorContext.getProject().getNaturalName());
        GitHubRepository githubRepository = createGitHubRepository(authToken, repoName, repoDescription, gitHubUser);

        pushToGithubRepository(generatorContext, gitHubUser, githubRepository, accessToken);

        return githubRepository;
    }

    private void pushToGithubRepository(GeneratorContext generatorContext, GitHubUser gitHubUser,
                                        GitHubRepository githubRepository, AccessToken accessToken) {
        Path repoPath = null;
        try {
            repoPath = Files.createTempDirectory(Paths.get(TMP_DIR), REPO_PREFIX);
            generateAppLocally(generatorContext, repoPath);
            GitHubUtil.initAndPushToGitHubRepository(
                    githubRepository, gitHubUser, repoPath, accessToken.getAccessToken());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully pushed application to {}", githubRepository);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to push to created repository: " + githubRepository.getUrl());
        } finally {
            try {
                if (repoPath != null) {
                    deleteDirectory(repoPath);
                }
            } catch (IOException e) {
                LOG.error("Error cleaning up temporary project directory: {}", e.getMessage(), e);
            }
        }
    }

    private GitHubRepository createGitHubRepository(String authToken, String repoName, String repoDescription,
                                                    GitHubUser gitHubUser) {
        try {
            if (gitHubApiClient.getRepository(authToken, gitHubUser.getLogin(), repoName) != null) {
                throw new IllegalArgumentException("Repository " + repoName + " already exists.");
            }

            GitHubRepository githubRepository = gitHubApiClient.createRepository(authToken,
                    new GitHubRepository(repoName, repoDescription));

            if (LOG.isDebugEnabled()) {
                LOG.debug("Created repository {}", githubRepository);
            }
            return githubRepository;
        } catch (HttpClientResponseException e) {
            throw new RuntimeException("Failed to create repository " + repoName);
        }
    }

    private AccessToken getGitHubAccessToken(String code, String state) {
        try {
            return gitHubOAuthClient.accessToken(gitHubConfiguration.getClientId(),
                    gitHubConfiguration.getClientSecret(), code, state);
        } catch (HttpClientResponseException e) {
            throw new RuntimeException("Failed to get user access token.");
        }
    }

    private GitHubUser getGitHubUser(String authToken) {
        try {
            GitHubUser gitHubUser = gitHubApiClient.getUser(authToken);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Fetched user {}", gitHubUser);
            }
            return gitHubUser;
        } catch (HttpClientResponseException e) {
            throw new RuntimeException("Failed to get user.");
        }
    }

    /**
     * Generates the micronaut application into specified {@code repoPath} directory
     *
     * @param generatorContext context
     * @param repoPath         path
     * @throws IOException
     */
    protected void generateAppLocally(@NotNull GeneratorContext generatorContext, @NotNull Path repoPath) throws IOException {
        try {
            if (!Files.isDirectory(repoPath)) {
                throw new IllegalArgumentException(String.format("The path %s must be a directory!", repoPath.toString()));
            }

            OutputHandler outputHandler = new FileSystemOutputHandler(repoPath.toFile(), ConsoleOutput.NOOP);
            projectGenerator.generate(generatorContext.getApplicationType(),
                    generatorContext.getProject(),
                    outputHandler,
                    generatorContext);
        } catch (Exception e) {
            LOG.error("Error generating application: {}", e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
