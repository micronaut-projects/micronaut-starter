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
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.UserAgentParser;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.diff.FeatureDiffer;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.MicronautJdkVersionConfiguration;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A controller for performing Diffs.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/diff")
public class DiffController implements DiffOperations {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final ProjectGenerator projectGenerator;
    private final FeatureDiffer featureDiffer;
    private final Project project;

    /**
     * Default constructor.
     * @param projectGenerator The project generator
     * @param featureDiffer The feature differ
     */
    public DiffController(ProjectGenerator projectGenerator, FeatureDiffer featureDiffer) {
        this.projectGenerator = projectGenerator;
        this.featureDiffer = featureDiffer;
        this.project = NameUtils.parse("example");
    }

    /**
     * Returns a diff for the given application type and feature.
     *
     * @param type The application type
     * @param feature The feature
     * @param build The build tool
     * @param test The test framework
     * @param lang The lang
     * @param javaVersion The java version
     * @param requestInfo The request info
     * @return A string representing the difference
     */
    @Get(uri = "/{type}/feature/{feature}{?lang,build,test,javaVersion,name}",
            produces = MediaType.TEXT_PLAIN)
    @Override
    @ApiResponse(responseCode = "404", description = "If no difference is found")
    @ApiResponse(responseCode = "400", description = "If the supplied parameters are invalid")
    @ApiResponse(responseCode = "200", description = "A textual diff", content = @Content(mediaType = "text/plain"))
    public String diffFeature(
            @NotNull ApplicationType type,
            @Nullable String name,
            @NonNull @NotBlank String feature,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) {

        ProjectGenerator projectGenerator;
        GeneratorContext generatorContext;
        try {
            Project project = name != null ? NameUtils.parse(name) : this.project;
            Language language = lang != null ? lang : Language.DEFAULT_OPTION;
            Options options = new Options(
                    language,
                    test != null ? test : language.getDefaults().getTest(),
                    build != null ? build : language.getDefaults().getBuild(),
                    javaVersion != null ? javaVersion : MicronautJdkVersionConfiguration.DEFAULT_OPTION
            );
            projectGenerator = this.projectGenerator;
            generatorContext = projectGenerator.createGeneratorContext(
                    type,
                    project,
                    options,
                    UserAgentParser.getOperatingSystem(requestInfo.getUserAgent()),
                    Collections.singletonList(feature),
                    ConsoleOutput.NOOP
            );
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return diff(projectGenerator, generatorContext);
    }

    /**
     * Diffs the whole application for all selected features.
     * @param type The application type
     * @param name The name of the application
     * @param features The features
     * @param build The build tool
     * @param test The test framework
     * @param lang The lang
     * @param requestInfo The request info
     * @return An HTTP response that emits a writable
     */
    @Get(uri = "/{type}/{name}{?features,lang,build,test,javaVersion}", produces = MediaType.TEXT_PLAIN)
    @Override
    @ApiResponse(responseCode = "404", description = "If no difference is found")
    @ApiResponse(responseCode = "400", description = "If the supplied parameters are invalid")
    @ApiResponse(responseCode = "200", description = "A textual diff", content = @Content(mediaType = "text/plain"))
    public String diffApp(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) throws IOException {
        ProjectGenerator projectGenerator;
        GeneratorContext generatorContext;
        try {
            Project project = name != null ? NameUtils.parse(name) : this.project;
            Language language = lang != null ? lang : Language.DEFAULT_OPTION;
            Options options = new Options(
                    language,
                    test != null ? test : language.getDefaults().getTest(),
                    build != null ? build : language.getDefaults().getBuild(),
                    javaVersion != null ? javaVersion : MicronautJdkVersionConfiguration.DEFAULT_OPTION
            );
            projectGenerator = this.projectGenerator;
            generatorContext = projectGenerator.createGeneratorContext(
                    type,
                    project,
                    options,
                    UserAgentParser.getOperatingSystem(requestInfo.getUserAgent()),
                    features != null ? features : Collections.emptyList(),
                    ConsoleOutput.NOOP
            );
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return diff(projectGenerator, generatorContext);
    }

    private String diff(ProjectGenerator projectGenerator, GeneratorContext generatorContext) {
        final StringBuilder sb = new StringBuilder();
        try {
            featureDiffer.produceDiff(
                    projectGenerator,
                    generatorContext,
                    new ConsoleOutput() {
                        @Override
                        public void out(String message) {
                            sb.append(message).append(LINE_SEPARATOR);
                        }

                        @Override
                        public void err(String message) {
                            // will never be called
                        }

                        @Override
                        public void warning(String message) {
                            // will never be called
                        }

                        @Override
                        public boolean showStacktrace() {
                            return false;
                        }

                        @Override
                        public boolean verbose() {
                            return false;
                        }
                    }
            );
            return sb.toString();
        } catch (Exception e) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not produce diff: " + e.getMessage());
        }
    }
}
