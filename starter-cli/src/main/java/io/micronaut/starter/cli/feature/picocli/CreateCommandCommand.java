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
package io.micronaut.starter.cli.feature.picocli;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.picocli.lang.groovy.PicocliGroovyApplication;
import io.micronaut.starter.feature.picocli.lang.java.PicocliJavaApplication;
import io.micronaut.starter.feature.picocli.lang.kotlin.PicocliKotlinApplication;
import io.micronaut.starter.feature.picocli.test.junit.PicocliJunit;
import io.micronaut.starter.feature.picocli.test.kotest.PicocliKoTest;
import io.micronaut.starter.feature.picocli.test.spock.PicocliSpock;
import io.micronaut.starter.options.AbstractTestRockerModelProvider;
import io.micronaut.starter.options.JunitRockerModelProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import static io.micronaut.starter.application.ApplicationType.CLI;
import static io.micronaut.starter.feature.picocli.test.PicocliTestFeature.PATH;
import static io.micronaut.starter.options.Language.GROOVY;
import static io.micronaut.starter.options.Language.JAVA;
import static io.micronaut.starter.options.Language.KOTLIN;

@Command(name = "create-command", description = "Creates a CLI command")
@Prototype
public class CreateCommandCommand extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "COMMAND-NAME", description = "The name of the command class to create")
    protected String name;

    protected final PicocliJavaApplication javaApplication;
    protected final PicocliGroovyApplication groovyApplication;
    protected final PicocliKotlinApplication kotlinApplication;
    protected final PicocliJunit junit;
    protected final PicocliSpock spock;
    protected final PicocliKoTest koTest;

    public CreateCommandCommand(@Parameter CodeGenConfig config,
                                PicocliJavaApplication javaApplication,
                                PicocliGroovyApplication groovyApplication,
                                PicocliKotlinApplication kotlinApplication,
                                PicocliJunit junit,
                                PicocliSpock spock,
                                PicocliKoTest koTest) {
        super(config);
        this.javaApplication = javaApplication;
        this.junit = junit;
        this.spock = spock;
        this.koTest = koTest;
        this.groovyApplication = groovyApplication;
        this.kotlinApplication = kotlinApplication;
    }

    @Override
    public boolean applies() {
        return config.getApplicationType() == CLI;
    }

    @Override
    public Integer call() throws Exception {

        Project project = getProject(name);
        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == JAVA) {
            renderResult = templateRenderer.render(javaApplication.getTemplate(project), overwrite);
        } else if (config.getSourceLanguage() == GROOVY) {
            renderResult = templateRenderer.render(groovyApplication.getTemplate(project), overwrite);
        } else if (config.getSourceLanguage() == KOTLIN) {
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

        Language rockerTemplateLanguage = (config.getTestFramework().getSupportedLanguages().contains(config.getSourceLanguage())) ?
                config.getSourceLanguage() : config.getTestFramework().getDefaultLanguage();
        TestRockerModelProvider testRockerModelProvider = new CustomTestRockerModelProvider(project, junit.getJunitRockerModelProvider(project)) {
            @Override
            public RockerModel spock() {
                return spock.getModel(getProject());
            }

            @Override
            public RockerModel koTest() {
                return koTest.getModel(project);
            }
        };
        RockerModel rockerModel = testRockerModelProvider.findModel(rockerTemplateLanguage, config.getTestFramework());
        String path = config.getTestFramework().getSourcePath(PATH, rockerTemplateLanguage);
        RockerTemplate rockerTemplate = new RockerTemplate(path, rockerModel);
        renderResult = templateRenderer.render(rockerTemplate, overwrite);

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

    public abstract static class CustomTestRockerModelProvider extends AbstractTestRockerModelProvider {

        protected final JunitRockerModelProvider junitRockerModelProvider;

        public CustomTestRockerModelProvider(Project project,
                                             JunitRockerModelProvider junitRockerModelProvider) {
            super(project);
            this.junitRockerModelProvider = junitRockerModelProvider;
        }

        @Override
        public RockerModel javaJunit() {
            return junitRockerModelProvider.javaJunit();
        }

        @Override
        public RockerModel groovyJunit() {
            return junitRockerModelProvider.groovyJunit();
        }

        @Override
        public RockerModel kotlinJunit() {
            return junitRockerModelProvider.kotlinJunit();
        }
    }
}
