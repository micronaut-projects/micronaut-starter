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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Header;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;

import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * Defines the signature for creating an application in Github repository.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
public interface GitHubCreateOperation {

    /**
     * Creates and push application to GitHub repository.
     * @param type The application type
     * @param name The name of the application and GitHub repository
     * @param features The features
     * @param buildTool The build tool
     * @param testFramework The test framework
     * @param lang The lang
     * @param javaVersion The java version
     * @param code The github code
     * @return An information about newly created GitHub repository
     */
    HttpResponse<GitHubCreateDTO> createApp(
            @NonNull ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @NonNull String code,
            @NonNull String state,
            @Nullable @Header(HttpHeaders.USER_AGENT) String userAgent,
            @NonNull RequestInfo requestInfo
    );
}
