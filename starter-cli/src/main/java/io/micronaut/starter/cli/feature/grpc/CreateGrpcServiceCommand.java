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
package io.micronaut.starter.cli.feature.grpc;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.cli.feature.grpc.template.groovyService;
import io.micronaut.starter.cli.feature.grpc.template.javaService;
import io.micronaut.starter.cli.feature.grpc.template.kotlinService;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

@CommandLine.Command(name = "create-grpc-service", description = "Creates a gRPC service with proto file and associated test")
@Prototype
public class CreateGrpcServiceCommand extends CodeGenCommand {

    @ReflectiveAccess
    @CommandLine.Parameters(paramLabel = "SERVICE-NAME", description = "The name of the service to create")
    String serviceName;

    public CreateGrpcServiceCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    @Override
    public boolean applies() {
        return config.getApplicationType() == ApplicationType.GRPC;
    }

    @Override
    public Integer call() throws Exception {

        CreateProtoServiceCommand command = getCommand(CreateProtoServiceCommand.class);
        command.serviceName = serviceName;
        int exitCode = command.call();
        if (exitCode > 0) {
            return exitCode;
        }

        Project project = getProject(serviceName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        String path = "/{packagePath}/{className}";
        path = config.getSourceLanguage().getSourcePath(path);
        RockerModel rockerModel = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            rockerModel = javaService.template(project);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            rockerModel = groovyService.template(project);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            rockerModel = kotlinService.template(project);
        }
        renderResult = templateRenderer.render(new RockerTemplate(path, rockerModel), overwrite);

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered gRPC service to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
