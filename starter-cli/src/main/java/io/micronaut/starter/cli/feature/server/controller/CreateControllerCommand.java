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
package io.micronaut.starter.cli.feature.server.controller;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.feature.server.template.groovyController;
import io.micronaut.starter.feature.server.template.groovyJunit;
import io.micronaut.starter.feature.server.template.javaController;
import io.micronaut.starter.feature.server.template.javaJunit;
import io.micronaut.starter.feature.server.template.koTest;
import io.micronaut.starter.feature.server.template.kotlinController;
import io.micronaut.starter.feature.server.template.kotlinJunit;
import io.micronaut.starter.feature.server.template.spock;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.List;

import static io.micronaut.starter.options.Language.GROOVY;
import static io.micronaut.starter.options.Language.JAVA;
import static io.micronaut.starter.options.Language.KOTLIN;

@Command(name = "create-controller", description = "Creates a controller and associated test")
public class CreateControllerCommand extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "CONTROLLER-NAME", description = "The name of the controller to create")
    protected String controllerName;

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
        if (config.getSourceLanguage() == JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate(JAVA.getSrcDir() + "/{packagePath}/{className}Controller." + JAVA.getExtension(), javaController.template(project)), overwrite);
        } else if (config.getSourceLanguage() == GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate(GROOVY.getSrcDir() + "/{packagePath}/{className}Controller." + GROOVY.getExtension(), groovyController.template(project)), overwrite);
        } else if (config.getSourceLanguage() == KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate(KOTLIN.getSrcDir() + "/{packagePath}/{className}Controller." + KOTLIN.getExtension(), kotlinController.template(project)), overwrite);
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

        String path = "/{packagePath}/{className}Controller";
        path = config.getTestFramework().getSourcePath(path, config.getSourceLanguage());
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(
                spock.template(project, true),
                javaJunit.template(project, true),
                groovyJunit.template(project, true),
                kotlinJunit.template(project, true),
                koTest.template(project, true));
        RockerModel rockerModel = provider.findModel(config.getSourceLanguage(), config.getTestFramework());
        renderResult = templateRenderer.render(new RockerTemplate(path, rockerModel), overwrite);

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
