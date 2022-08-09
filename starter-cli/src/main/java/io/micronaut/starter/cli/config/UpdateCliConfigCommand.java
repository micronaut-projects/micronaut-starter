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
package io.micronaut.starter.cli.config;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.feature.cli;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.micronaut.starter.application.ApplicationType.CLI;
import static io.micronaut.starter.application.ApplicationType.DEFAULT;
import static io.micronaut.starter.application.ApplicationType.FUNCTION;
import static io.micronaut.starter.application.ApplicationType.GRPC;
import static io.micronaut.starter.application.ApplicationType.MESSAGING;

@Command(name = "update-cli-config", description = "Replaces the CLI configuration with the updated format")
@Prototype
public class UpdateCliConfigCommand extends CodeGenCommand {

    private static final Map<ApplicationType, String> COMMANDS = new LinkedHashMap<>(ApplicationType.values().length);

    static {
        COMMANDS.put(DEFAULT, "create-app");
        COMMANDS.put(CLI, "create-cli-app");
        COMMANDS.put(FUNCTION, "create-function-app");
        COMMANDS.put(GRPC, "create-grpc-app");
        COMMANDS.put(MESSAGING, "create-messaging-app");
    }

    @Inject
    public UpdateCliConfigCommand(@Parameter CodeGenConfig codeGenConfig) {
        super(codeGenConfig);
    }

    public UpdateCliConfigCommand(CodeGenConfig config,
                                  ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                                  ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.isLegacy();
    }

    @Override
    public Integer call() throws Exception {
        TemplateRenderer templateRenderer = getTemplateRenderer();

        templateRenderer.render(new RockerTemplate(Template.ROOT, "micronaut-cli.yml", cli.template(config.getSourceLanguage(),
                config.getTestFramework(),
                config.getBuildTool(),
                //Only the package is used
                NameUtils.parse(config.getDefaultPackage() + ".Ignored"),
                config.getFeatures(),
                config.getApplicationType())), true);

        out("In order to use code generation commands that are dependent on a feature, you may need to modify the feature list to include any features that are in use.");
        out("For example, in order to execute `mn create-kafka-listener`, `kafka` must be in the list of features in `micronaut-cli.yml`.");
        out(String.format("For a list of available features, run `mn %s --list-features`", COMMANDS.get(config.getApplicationType())));

        return 0;
    }
}
