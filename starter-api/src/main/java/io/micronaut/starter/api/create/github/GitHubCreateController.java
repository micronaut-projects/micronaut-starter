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
package io.micronaut.starter.api.create.github;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.client.github.v3.GitHubRepository;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;

/**
 * GitHub create controller.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
@Controller
@ExecuteOn(TaskExecutors.IO)
@Requires(beans = {GitHubCreateService.class, GitHubRedirectService.class})
public class GitHubCreateController implements GitHubCreateOperation {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubCreateController.class);
    private final GitHubCreateService gitHubCreateService;
    private final GitHubRedirectService redirectService;

    public GitHubCreateController(GitHubCreateService gitHubCreateService,
                                  GitHubRedirectService redirectService) {
        this.gitHubCreateService = gitHubCreateService;
        this.redirectService = redirectService;
    }

    /**
     * Creates an application in GitHub repository, generating a json file as the response.
     *
     * @param type     The application type The application type
     * @param name     The name of the application The name of the application
     * @param features The features The chosen features
     * @param build    The build type (optional, defaults to Gradle)
     * @param test     The test framework (optional, defaults to JUnit)
     * @param lang     The language (optional, defaults to Java)
     * @return A json containing the generated application details.
     */
    @Override
    @Get(uri = "/github/{type}/{name}{?features,lang,build,test,javaVersion,code,state}", produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Created GitHub repository containing the generated application. In case " +
                            "the configuration contains launcher URI the redirect to launcher is sent.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON
                    )
            ),
            @ApiResponse(
                    responseCode = "307",
                    description = "Redirects to GitHub OAuth API to obtain user authorisation code before creating " +
                            "the GitHub repository."
            ),
            @ApiResponse(
                    responseCode = "307",
                    description = "Redirects back to launcher in case of successfully created GitHub repository."
            )})
    public HttpResponse<GitHubCreateDTO> createApp(
            @NonNull ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Nullable String code,
            @Nullable String state,
            @Nullable @Header(HttpHeaders.USER_AGENT) String userAgent,
            @Parameter(hidden = true) @NonNull RequestInfo requestInfo) {
        URI launcherURI = redirectService.getLauncherURI();
        try {
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
                return HttpResponse.temporaryRedirect(redirectService.constructOAuthRedirectUrl(requestInfo));
            } else {
                GitHubRepository repository = gitHubCreateService.creatApp(
                        type, name, features, build, test, lang, javaVersion, code, state, userAgent);

                if (launcherURI == null) {
                    return HttpResponse.ok(new GitHubCreateDTO(repository.getUrl(), repository.getCloneUrl(), repository.getHtmlUrl()));
                } else {
                    return HttpResponse.temporaryRedirect(redirectService.constructLauncherRedirectUrl(repository));
                }
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage());
            }

            if (launcherURI == null) {
                throw e;
            } else {
                return HttpResponse.temporaryRedirect(redirectService.constructLauncherErrorRedirectUrl(e.getMessage()));
            }
        }
    }

    /**
     * Endpoint handles GitHub OAuth authorisation errors.
     *
     * @param error            error code
     * @param errorDescription description
     * @return Http redirects
     * @see <a href="https://docs.github.com/en/free-pro-team@latest/developers/apps/troubleshooting-oauth-app-access-token-request-errors">Troubleshooting OAuth App access token request errors</a>
     */
    @Get(uri = "/github{?error,error_description}", produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "307",
                    description = "Forwarded GitHub OAuth error message."
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns GitHub OAuth application callback error."
            )})
    public HttpResponse<String> handleCallback(
            @Nullable String error,
            @Nullable @QueryValue("error_description") String errorDescription) {
        URI redirect;
        if (!StringUtils.isEmpty(error)) {
            redirect = redirectService.constructLauncherErrorRedirectUrl(errorDescription);
        } else {
            redirect = redirectService.getLauncherURI();
        }

        if (redirect == null) {
            return HttpResponse.ok(errorDescription);
        } else {
            return HttpResponse.temporaryRedirect(redirect);
        }
    }
}
