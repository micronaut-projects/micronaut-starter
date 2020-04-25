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
package io.micronaut.starter.cli.feature.messaging.rabbitmq;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.cli.feature.messaging.rabbitmq.template.listener.groovyListener;
import io.micronaut.starter.cli.feature.messaging.rabbitmq.template.listener.javaListener;
import io.micronaut.starter.cli.feature.messaging.rabbitmq.template.listener.kotlinListener;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

@CommandLine.Command(name = "create-rabbitmq-listener", description = "Creates a listener interface for RabbitMQ")
@Prototype
public class CreateRabbitMQListener extends CodeGenCommand {

    @ReflectiveAccess
    @CommandLine.Parameters(paramLabel = "LISTENER", description = "The name of the listener to create")
    String listenerName;

    public CreateRabbitMQListener(@Parameter CodeGenConfig config) {
        super(config);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("rabbitmq");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(listenerName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}.java", javaListener.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}.groovy", groovyListener.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}.kt", kotlinListener.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered RabbitMQ listener to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
