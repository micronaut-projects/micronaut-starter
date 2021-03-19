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
package io.micronaut.starter.api.preview;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.Relationship;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.api.create.AbstractCreateController;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.MapOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Previews an application contents.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/preview")
public class PreviewController extends AbstractCreateController implements PreviewOperations {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateController.class);

    /**
     * Default constructor.
     *
     * @param projectGenerator The project generator
     * @param eventPublisher The event publisher
     */
    public PreviewController(ProjectGenerator projectGenerator, ApplicationEventPublisher eventPublisher) {
        super(projectGenerator, eventPublisher);
    }

    /**
     * Previews the contents of a generated application..
     * @param type The application type The application type
     * @param name The name of the application The name of the application
     * @param features The features The chosen features
     * @param build The build type (optional, defaults to Gradle)
     * @param test The test framework (optional, defaults to JUnit)
     * @param lang The language (optional, defaults to Java)
     * @return A preview of the application contents.
     */
    @Get(uri = "/{type}/{name}{?features,lang,build,test,javaVersion}", produces = MediaType.APPLICATION_JSON)
    @Override
    public PreviewDTO previewApp(
            ApplicationType type,
            String name,
            @Nullable List<String> features,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) throws IOException {
        try {
            Project project = NameUtils.parse(name);
            MapOutputHandler outputHandler = new MapOutputHandler();
            projectGenerator.generate(type,
                    project,
                    new Options(
                            lang,
                            test != null ? test.toTestFramework() : null,
                            build == null ? BuildTool.DEFAULT_OPTION : build,
                            javaVersion == null ? JdkVersion.DEFAULT_OPTION : javaVersion),
                    getOperatingSystem(requestInfo.getUserAgent()),
                    features == null ? Collections.emptyList() : features,
                    outputHandler,
                    ConsoleOutput.NOOP);
            Map<String, String> contents = outputHandler.getProject();
            PreviewDTO previewDTO = new PreviewDTO(contents);
            previewDTO.addLink(Relationship.CREATE, requestInfo.link(Relationship.CREATE, type));
            previewDTO.addLink(Relationship.SELF, requestInfo.self());
            return previewDTO;
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOG.error("Error generating application: " + e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

}
