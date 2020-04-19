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
package io.micronaut.starter.feature.grpc.command;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CodeGenCommand;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.grpc.template.proto;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

@CommandLine.Command(name = "create-proto-service", description = "Creates a protobuf file for the given ame")
@Prototype
public class CreateProtoServiceCommand extends CodeGenCommand {
    
    @CommandLine.Parameters(paramLabel = "SERVICE-NAME", description = "The name of the service to create")
    String serviceName;

    public CreateProtoServiceCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    @Override
    public boolean applies() {
        return config.getCommand() == MicronautCommand.CREATE_GRPC_APP;
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(serviceName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = templateRenderer.render(new RockerTemplate("src/main/proto/{propertyName}.proto", proto.template(project)));

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered Proto service to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
    
}
