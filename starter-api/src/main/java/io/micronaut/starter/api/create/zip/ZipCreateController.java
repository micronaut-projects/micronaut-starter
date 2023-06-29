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
package io.micronaut.starter.api.create.zip;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.bind.annotation.Bindable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.api.create.AbstractCreateController;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Implements the {@link ZipCreateOperation} interface for applications.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller
@ExecuteOn(TaskExecutors.BLOCKING)
public class ZipCreateController extends AbstractCreateController implements ZipCreateOperation {

    public static final String MEDIA_TYPE_APPLICATION_ZIP = "application/zip";
    private static final Logger LOG = LoggerFactory.getLogger(ZipCreateController.class);

    /**
     * Default constructor.
     *
     * @param projectGenerator The project generator
     * @param eventPublisher   The event publisher
     */
    public ZipCreateController(ProjectGenerator projectGenerator, ApplicationEventPublisher eventPublisher) {
        super(projectGenerator, eventPublisher);
    }

    /**
     * Creates an application, generating a ZIP file as the response.
     *
     * @param type     The application type The application type
     * @param name     The name of the application The name of the application
     * @param features The features The chosen features
     * @param build    The build type (optional, defaults to Gradle)
     * @param test     The test framework (optional, defaults to JUnit)
     * @param lang     The language (optional, defaults to Java)
     * @return A ZIP file containing the generated application.
     */
    @Override
    @Get(uri = "/create/{type}/{name}{?features,lang,build,test,javaVersion}", produces = MEDIA_TYPE_APPLICATION_ZIP)
    @ApiResponse(
            description = "A ZIP file containing the generated application.",
            content = @Content(
                    mediaType = MEDIA_TYPE_APPLICATION_ZIP
            )
    )
    public HttpResponse<Writable> createApp(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Nullable @Header(HttpHeaders.USER_AGENT) String userAgent) {
        return generateAppIntoZipFile(type, name, features, build, test, lang, javaVersion, userAgent);
    }

    /**
     * Creates the default application type using the name of the given Zip.
     *
     * @param type     The type
     * @param name     The ZIP name
     * @param features The features
     * @param build    The build tool
     * @param test     The test framework
     * @param lang     The language
     * @return A Zip file containing the application
     */
    @Get(uri = "/{name}.zip{?type,features,lang,build,test}", produces = MEDIA_TYPE_APPLICATION_ZIP)
    @ApiResponse(
            description = "A ZIP file containing the generated application.",
            content = @Content(
                    mediaType = MEDIA_TYPE_APPLICATION_ZIP
            )
    )
    public HttpResponse<Writable> createZip(
            @Bindable(defaultValue = "default") ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_]+") @NotBlank String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Nullable @Header("User-Agent") String userAgent) {
        return generateAppIntoZipFile(type, name, features, build, test, lang, javaVersion, userAgent);
    }

    public HttpResponse<Writable> generateAppIntoZipFile(
            @NotNull ApplicationType type,
            @NotNull String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Nullable String userAgent) {

        GeneratorContext generatorContext = createProjectGeneratorContext(type, name, features, buildTool, testFramework, lang, javaVersion, userAgent);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                try {
                    final Project project = generatorContext.getProject();
                    projectGenerator.generate(type,
                            project,
                            new ZipOutputHandler(project.getName(), outputStream),
                            generatorContext);

                    outputStream.flush();
                } catch (Exception e) {
                    LOG.error("Error generating application: " + e.getMessage(), e);
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) {
                // no-op, output stream used
            }
        });
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getFilename(generatorContext.getProject()));
    }

    /**
     * @return The file name to return.
     * @param project The project
     */
    protected @NonNull String getFilename(@NonNull Project project) {
        return project.getName() + ".zip";
    }
}
