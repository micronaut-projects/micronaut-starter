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
package io.micronaut.starter.api.create;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.bind.annotation.Bindable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Implements the {@link CreateOperation} interface for applications.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/")
public class CreateController extends AbstractCreateController implements CreateOperation {

    /**
     * Default constructor.
     *
     * @param projectGenerator The project generator
     * @param eventPublisher The event publisher
     */
    public CreateController(ProjectGenerator projectGenerator, ApplicationEventPublisher eventPublisher) {
        super(projectGenerator, eventPublisher);
    }

    /**
     * Creates an application, generating a ZIP file as the response.
     * @param type The application type The application type
     * @param name The name of the application The name of the application
     * @param features The features The chosen features
     * @param build The build type (optional, defaults to Gradle)
     * @param test The test framework (optional, defaults to JUnit)
     * @param lang The language (optional, defaults to Java)
     * @return A ZIP file containing the generated application.
     */
    @Override
    @Get(uri = "/create/{type}/{name}{?features,lang,build,test,javaVersion}", produces = "application/zip")
    @ApiResponse(
            description = "A ZIP file containing the generated application.",        
            content = @Content(
                    mediaType = "application/zip"
            )
    )
    public HttpResponse<Writable> createApp(
            ApplicationType type,
            String name,
            @Nullable List<String> features,
            BuildTool build,
            TestFramework test,
            Language lang,
            Integer javaVersion) {
        return super.createApp(type, name, features, build, test, lang, javaVersion);
    }

    /**
     * Creates the default application type using the name of the given Zip.
     * @param type The type
     * @param name The ZIP name
     * @param features The features
     * @param build The build tool
     * @param test The test framework
     * @param lang The language
     * @return A Zip file containing the application
     */
    @Get(uri = "/{name}.zip{?type,features,lang,build,test}", produces = "application/zip")
    @ApiResponse(
            description = "A ZIP file containing the generated application.",
            content = @Content(
                    mediaType = "application/zip"
            )
    )
    public HttpResponse<Writable> createZip(
            @Bindable(defaultValue = "default") ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_]+") @NotBlank String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable Integer javaVersion) {
        return super.createApp(type, name, features, build, test, lang, javaVersion);
    }
}
