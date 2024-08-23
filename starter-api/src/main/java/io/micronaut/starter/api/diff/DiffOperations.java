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
package io.micronaut.starter.api.diff;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.swagger.v3.oas.annotations.Parameter;
import org.reactivestreams.Publisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Operations for performing diffs.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface DiffOperations {
    /**
     * Previews an application.
     * @param type The application type
     * @param name The project name
     * @param feature The feature
     * @param buildTool The build tool
     * @param testFramework The test framework
     * @param lang The lang
     * @param requestInfo The request info
     * @return An HTTP response that emits a writable
     */
    String diffFeature(
            @NotNull ApplicationType type,
            @Nullable String name,
            @NotBlank @NonNull String feature,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) throws IOException;

    /**
     * Diffs the whole application for all selected features.
     * @param type The application type
     * @param name The name of the application
     * @param features The features
     * @param buildTool The build tool
     * @param testFramework The test framework
     * @param lang The lang
     * @param requestInfo The request info
     * @return An HTTP response that emits a writable
     */
    String diffApp(
            ApplicationType type,
            String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) throws IOException;
}
