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
package io.micronaut.starter.cli.feature.messaging.nats;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.cli.feature.messaging.nats.template.listener.groovyListener;
import io.micronaut.starter.cli.feature.messaging.nats.template.listener.javaListener;
import io.micronaut.starter.cli.feature.messaging.nats.template.listener.kotlinListener;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;

import static io.micronaut.starter.options.Language.GROOVY;
import static io.micronaut.starter.options.Language.JAVA;
import static io.micronaut.starter.options.Language.KOTLIN;

@Command(name = "create-nats-listener", description = "Creates a listener class for Nats.io")
@Prototype
public class CreateNatsListener extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "LISTENER", description = "The name of the listener to create")
    String listenerName;

    public CreateNatsListener(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateNatsListener(CodeGenConfig config,
                          ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                          ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("nats");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(listenerName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        String path = "/{packagePath}/{className}";
        path = config.getSourceLanguage().getSourcePath(path);
        RenderResult renderResult = null;
        RockerModel rockerModel = null;
        if (config.getSourceLanguage() == JAVA) {
            rockerModel = javaListener.template(project);
        } else if (config.getSourceLanguage() == GROOVY) {
            rockerModel = groovyListener.template(project);
        } else if (config.getSourceLanguage() == KOTLIN) {
            rockerModel = kotlinListener.template(project);
        }
        renderResult = templateRenderer.render(new RockerTemplate(path, rockerModel), overwrite);

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered Nats listener to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
