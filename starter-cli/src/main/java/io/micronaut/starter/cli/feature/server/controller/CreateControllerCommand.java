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
package io.micronaut.starter.cli.feature.server.controller;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.feature.server.template.*;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@CommandLine.Command(name = "create-controller", description = "Creates a controller and associated test")
public class CreateControllerCommand extends CodeGenCommand {

    @ReflectiveAccess
    @CommandLine.Parameters(paramLabel = "CONTROLLER-NAME", description = "The name of the controller to create")
    String controllerName;

    private final List<Feature> features;

    @Inject
    public CreateControllerCommand(@Parameter CodeGenConfig config, List<Feature> features) {
        super(config);
        this.features = features;
    }

    public CreateControllerCommand(CodeGenConfig config,
                                   ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                                   ConsoleOutput consoleOutput,
                                   List<Feature> features) {
        super(config, outputHandlerSupplier, consoleOutput);
        this.features = features;
    }

    @Override
    public boolean applies() {
        List<String> selectedFeatures = config.getFeatures();
        return features.stream()
                .filter(ServerFeature.class::isInstance)
                .map(Feature::getName)
                .anyMatch(selectedFeatures::contains);
    }

    @Override
    public Integer call() throws Exception {

        Project project = getProject(controllerName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}Controller.java", javaController.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}Controller.groovy", groovyController.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}Controller.kt", kotlinController.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered controller to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        renderResult = null;
        if (config.getTestFramework() == TestFramework.JUNIT) {
            if (config.getSourceLanguage() == Language.JAVA) {
                renderResult = templateRenderer.render(new RockerTemplate("src/test/java/{packagePath}/{className}ControllerTest.java", javaJunit.template(project)), overwrite);
            } else if (config.getSourceLanguage() == Language.GROOVY) {
                renderResult = templateRenderer.render(new RockerTemplate("src/test/groovy/{packagePath}/{className}ControllerTest.groovy", groovyJunit.template(project)), overwrite);
            } else if (config.getSourceLanguage() == Language.KOTLIN) {
                renderResult = templateRenderer.render(new RockerTemplate("src/test/kotlin/{packagePath}/{className}ControllerTest.kt", kotlinJunit.template(project)), overwrite);
            }
        } else if (config.getTestFramework() == TestFramework.SPOCK) {
            renderResult = templateRenderer.render(new RockerTemplate("src/test/groovy/{packagePath}/{className}ControllerSpec.groovy", spock.template(project)), overwrite);
        } else if (config.getTestFramework() == TestFramework.KOTLINTEST) {
            renderResult = templateRenderer.render(new RockerTemplate("src/test/kotlin/{packagePath}/{className}ControllerTest.kt", kotlinTest.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered test to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
