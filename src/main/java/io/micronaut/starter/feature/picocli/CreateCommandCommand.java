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
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import java.util.function.Consumer;

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
        return config.getCommand() == MicronautCommand.CREATE_CLI;
    }

    @Override
    public Integer call() throws Exception {

        Project project = getProject(name, config);
        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        if (config.getSourceLanguage() == Language.java) {
            templateRenderer.render(javaApplication.getTemplate(project), (path) -> {
                out("Rendered command to " + path);
            });
        } else if (config.getSourceLanguage() == Language.groovy) {
            templateRenderer.render(groovyApplication.getTemplate(project), (path) -> {
                out("Rendered command to " + path);
            });
        } else if (config.getSourceLanguage() == Language.kotlin) {
            templateRenderer.render(kotlinApplication.getTemplate(project), (path) -> {
                out("Rendered command to " + path);
            });
        }

        Consumer<String> testOutput = (path) -> {
            out("Rendered command test to " + path);
        };

        if (config.getTestFramework() == TestFramework.junit) {
            templateRenderer.render(junit.getTemplate(config.getSourceLanguage(), project), testOutput);
        } else if (config.getTestFramework() == TestFramework.spock) {
            templateRenderer.render(spock.getTemplate(project), testOutput);
        } else if (config.getTestFramework() == TestFramework.kotlintest) {
            templateRenderer.render(kotlinTest.getTemplate(project), testOutput);
        }

        return 0;
    }
}
