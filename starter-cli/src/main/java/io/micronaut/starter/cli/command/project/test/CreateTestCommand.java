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
package io.micronaut.starter.cli.command.project.test;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.test.template.groovyJunit;
import io.micronaut.starter.feature.test.template.javaJunit;
import io.micronaut.starter.feature.test.template.koTest;
import io.micronaut.starter.feature.test.template.kotlinJunit;
import io.micronaut.starter.feature.test.template.spock;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;

@Command(name = "create-test", description = "Creates a simple test for the project's testing framework")
@Prototype
public class CreateTestCommand extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "TEST-NAME", description = "The name of the test class to create")
    protected String testName;

    public CreateTestCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateTestCommand(CodeGenConfig config,
                             ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                             ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return true;
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(testName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        final String path = "/{packagePath}/{className}";
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(
                spock.template(project, true),
                javaJunit.template(project, true),
                groovyJunit.template(project, true),
                kotlinJunit.template(project, true),
                koTest.template(project, true));

        RockerModel rockerModel = provider.findModel(config.getSourceLanguage(), config.getTestFramework());
        String testPath = config.getTestFramework().getSourcePath(path, config.getSourceLanguage());
        RockerTemplate rockerTemplate = new RockerTemplate(testPath, rockerModel);
        RenderResult renderResult = templateRenderer.render(rockerTemplate);

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
