/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.cli.command.project.job;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;

@CommandLine.Command(name = "create-job", description = "Creates a job with scheduled method")
@Prototype
public class CreateJobCommand extends CodeGenCommand {

    @ReflectiveAccess
    @CommandLine.Parameters(paramLabel = "JOB-NAME", description = "The name of the job class to create")
    String jobName;

    @Inject
    public CreateJobCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateJobCommand(CodeGenConfig config,
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
        Project project = getProject(jobName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        String path = "/{packagePath}/{className}";
        path = config.getSourceLanguage().getSourcePath(path);
        RockerModel rockerModel = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            rockerModel = javaJob.template(project);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            rockerModel = groovyJob.template(project);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            rockerModel = kotlinJob.template(project);
        }
        renderResult = templateRenderer.render(new RockerTemplate(path, rockerModel), overwrite);

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered job to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
