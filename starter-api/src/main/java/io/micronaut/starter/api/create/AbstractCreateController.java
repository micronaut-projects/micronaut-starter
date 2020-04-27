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

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.event.ApplicationGeneratingEvent;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.*;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of a create controller.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractCreateController implements CreateOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateController.class);
    protected final ProjectGenerator projectGenerator;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Abstract implementation of {@link CreateOperation}.
     * @param projectGenerator The project generator
     * @param eventPublisher The event publisher
     */
    protected AbstractCreateController(
            ProjectGenerator projectGenerator,
            ApplicationEventPublisher eventPublisher) {
        this.projectGenerator = projectGenerator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public HttpResponse<Writable> createApp(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) {
        Project project;
        try {
            project = NameUtils.parse(name);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid project name: " + e.getMessage());
        }

        GeneratorContext generatorContext;
        try {
            generatorContext = projectGenerator.createGeneratorContext(
                    type,
                    project,
                    new Options(lang, testFramework, buildTool == null ? BuildTool.GRADLE : buildTool, javaVersion != null ? javaVersion : JdkVersion.JDK_8),
                    features != null ? features : Collections.emptyList(),
                    ConsoleOutput.NOOP
            );

            try {
                eventPublisher.publishEvent(new ApplicationGeneratingEvent(generatorContext));
            } catch (Exception e) {
                LOG.warn("Error firing application generated event: " + e.getMessage(), e);
            }
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                try {
                    projectGenerator.generate(type,
                            project,
                            new ZipOutputHandler(outputStream),
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
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getFilename(project));
    }

    /**
     * @return The file name to return.
     * @param project The project
     */
    protected @Nonnull String getFilename(@NonNull Project project) {
        return project.getName() + ".zip";
    }
}
