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
package io.micronaut.starter.cli.feature.messaging.mqtt;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.cli.feature.messaging.mqtt.template.producer.groovyProducer;
import io.micronaut.starter.cli.feature.messaging.mqtt.template.producer.javaProducer;
import io.micronaut.starter.cli.feature.messaging.mqtt.template.producer.kotlinProducer;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;

import static io.micronaut.starter.options.Language.GROOVY;
import static io.micronaut.starter.options.Language.JAVA;
import static io.micronaut.starter.options.Language.KOTLIN;

@Command(name = "create-mqtt-publisher", description = "Creates a publisher class for MQTT")
@Prototype
public class CreateMqttPublisher extends CodeGenCommand {

    @ReflectiveAccess
    @Parameters(paramLabel = "PUBLISHER", description = "The name of the publisher to create")
    String publisherName;

    @Inject
    public CreateMqttPublisher(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateMqttPublisher(CodeGenConfig config,
                               ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                               ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("mqtt") || config.getFeatures().contains("mqttv3");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(publisherName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        String path = "/{packagePath}/{className}";
        path = config.getSourceLanguage().getSourcePath(path);
        RockerModel rockerModel = null;
        String version = null;
        if (config.getFeatures().contains("mqtt")) {
            version = "v5";
        } else if (config.getFeatures().contains("mqttv3")) {
            version = "v3";
        }

        if (config.getSourceLanguage() == JAVA) {
            rockerModel = javaProducer.template(project, version);
        } else if (config.getSourceLanguage() == GROOVY) {
            rockerModel = groovyProducer.template(project, version);
        } else if (config.getSourceLanguage() == KOTLIN) {
            rockerModel = kotlinProducer.template(project, version);
        }
        renderResult = templateRenderer.render(new RockerTemplate(path, rockerModel), overwrite);

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered MQTT publisher to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
