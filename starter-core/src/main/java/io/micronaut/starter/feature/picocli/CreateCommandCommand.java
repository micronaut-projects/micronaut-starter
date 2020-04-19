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
package io.micronaut.starter.feature.picocli;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CodeGenCommand;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.picocli.lang.groovy.PicocliGroovyApplication;
import io.micronaut.starter.feature.picocli.lang.java.PicocliJavaApplication;
import io.micronaut.starter.feature.picocli.lang.kotlin.PicocliKotlinApplication;
import io.micronaut.starter.feature.picocli.test.junit.PicocliJunit;
import io.micronaut.starter.feature.picocli.test.kotlintest.PicocliKotlinTest;
import io.micronaut.starter.feature.picocli.test.spock.PicocliSpock;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

@CommandLine.Command(name = "create-command", description = "Creates a CLI command")
@Prototype
public class CreateCommandCommand extends CodeGenCommand {

    @CommandLine.Parameters(paramLabel = "COMMAND-NAME", description = "The name of the command class to create")
    String name;

    private final PicocliJavaApplication javaApplication;
    private final PicocliGroovyApplication groovyApplication;
    private final PicocliKotlinApplication kotlinApplication;
    private final PicocliJunit junit;
    private final PicocliSpock spock;
    private final PicocliKotlinTest kotlinTest;

    public CreateCommandCommand(@Parameter CodeGenConfig config,
                                PicocliJavaApplication javaApplication,
                                PicocliGroovyApplication groovyApplication,
                                PicocliKotlinApplication kotlinApplication,
                                PicocliJunit junit,
                                PicocliSpock spock,
                                PicocliKotlinTest kotlinTest) {
        super(config);
        this.javaApplication = javaApplication;
        this.junit = junit;
        this.spock = spock;
        this.kotlinTest = kotlinTest;
        this.groovyApplication = groovyApplication;
        this.kotlinApplication = kotlinApplication;
    }

    @Override
    public boolean applies() {
        return config.getCommand() == MicronautCommand.CREATE_CLI_APP;
    }

    @Override
    public Integer call() throws Exception {

        Project project = getProject(name);
        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.java) {
            renderResult = templateRenderer.render(javaApplication.getTemplate(project), overwrite);
        } else if (config.getSourceLanguage() == Language.groovy) {
            renderResult = templateRenderer.render(groovyApplication.getTemplate(project), overwrite);
        } else if (config.getSourceLanguage() == Language.kotlin) {
            renderResult = templateRenderer.render(kotlinApplication.getTemplate(project), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("Rendered command to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        if (config.getTestFramework() == TestFramework.junit) {
            renderResult = templateRenderer.render(junit.getTemplate(config.getSourceLanguage(), project), overwrite);
        } else if (config.getTestFramework() == TestFramework.spock) {
            renderResult = templateRenderer.render(spock.getTemplate(project), overwrite);
        } else if (config.getTestFramework() == TestFramework.kotlintest) {
            renderResult = templateRenderer.render(kotlinTest.getTemplate(project), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("Rendered command test to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
