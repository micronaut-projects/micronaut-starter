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
package io.micronaut.starter.api.create;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.api.UserAgentParser;
import io.micronaut.starter.api.event.ApplicationGeneratingEvent;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.MicronautJdkVersionConfiguration;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of a create controller.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractCreateController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateController.class);
    protected final ProjectGenerator projectGenerator;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Abstract implementation for create controllers.
     * @param projectGenerator The project generator
     * @param eventPublisher The event publisher
     */
    protected AbstractCreateController(
            ProjectGenerator projectGenerator,
            ApplicationEventPublisher eventPublisher) {
        this.projectGenerator = projectGenerator;
        this.eventPublisher = eventPublisher;
    }

    public GeneratorContext createProjectGeneratorContext(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Nullable @Header(HttpHeaders.USER_AGENT) String userAgent) {
        Project project;
        try {
            project = NameUtils.parse(name);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid project name: " + e.getMessage());
        }

        GeneratorContext generatorContext;
        try {
            Language language = lang != null ? lang : Language.DEFAULT_OPTION;
            generatorContext = projectGenerator.createGeneratorContext(
                    type,
                    project,
                    new Options(lang,
                            testFramework != null ? testFramework.toTestFramework() : language.getDefaults().getTest(),
                            buildTool == null ? language.getDefaults().getBuild() : buildTool,
                            javaVersion == null ? MicronautJdkVersionConfiguration.DEFAULT_OPTION : javaVersion),
                    getOperatingSystem(userAgent),
                    features != null ? features : Collections.emptyList(),
                    ConsoleOutput.NOOP
            );

            try {
                eventPublisher.publishEvent(new ApplicationGeneratingEvent(generatorContext));
            } catch (Exception e) {
                LOG.warn("Error firing application generated event: {}", e.getMessage(), e);
            }
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return generatorContext;
    }

    protected OperatingSystem getOperatingSystem(String userAgent) {
        return UserAgentParser.getOperatingSystem(userAgent);
    }
}
