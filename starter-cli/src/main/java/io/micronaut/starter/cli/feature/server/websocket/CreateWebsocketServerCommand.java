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
package io.micronaut.starter.cli.feature.server.websocket;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
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

@Command(name = "create-websocket-server", description = "Creates a Websocket server")
@Prototype
public class CreateWebsocketServerCommand extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "SERVER-NAME", description = "The name of the server to create")
    String serverName;

    private final List<Feature> features;

    @Inject
    public CreateWebsocketServerCommand(@Parameter CodeGenConfig config, List<Feature> features) {
        super(config);
        this.features = features;
    }

    public CreateWebsocketServerCommand(CodeGenConfig config,
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
        Project project = getProject(serverName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}Server.java", javaWebsocketServer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}Server.groovy", groovyWebsocketServer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}Server.kt", kotlinWebsocketServer.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered websocket server to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
