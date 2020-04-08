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
package io.micronaut.starter;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.starter.command.BaseCommand;
import io.micronaut.starter.command.CodeGenCommand;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;


@CommandLine.Command(name = "mn", description = {
        "Micronaut CLI command line interface for generating projects and services.",
        "Commonly used commands are:",
        "  @|bold create-app|@ @|yellow NAME|@",
        "  @|bold create-cli-app|@ @|yellow NAME|@",
        "  @|bold create-federation|@ @|yellow NAME|@ @|yellow --services|@ @|yellow,italic SERVICE_NAME[,SERVICE_NAME]...|@",
        "  @|bold create-function|@ @|yellow NAME|@"},
        synopsisHeading = "@|bold,underline Usage:|@ ",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        commandListHeading = "%n@|bold,underline Commands:|@%n",
        subcommands = {
                CreateAppCommand.class,
                CreateCliCommand.class
        })
@Prototype
public class MicronautStarter extends BaseCommand implements Callable<Integer> {

    private static final BiFunction<Throwable, CommandLine, Integer> exceptionHandler = (e, commandLine) -> {
        BaseCommand command = commandLine.getCommand();
        command.err(e.getMessage());
        if (command.showStacktrace()) {
            e.printStackTrace(commandLine.getErr());
        }
        return 1;
    };

    public static void main(String[] args) {
        if (args.length == 0) {
            new InteractiveShell(createCommandLine(), MicronautStarter::execute, exceptionHandler).start();
        } else {
            System.exit(execute(args));
        }
    }

    static CommandLine createCommandLine() {
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext);
        }
    }

    static int execute(String[] args) {
        try (BeanContext beanContext = BeanContext.run()) {
            return createCommandLine(beanContext).execute(args);
        }
    }

    private static CommandLine createCommandLine(BeanContext beanContext) {
        MicronautStarter starter = beanContext.getBean(MicronautStarter.class);
        CommandLine commandLine = new CommandLine(starter, new CommandLine.IFactory() {
            CommandLine.IFactory defaultFactory = CommandLine.defaultFactory();

            @Override
            public <K> K create(Class<K> cls) throws Exception {
                Optional<K> bean = beanContext.findOrInstantiateBean(cls);
                if (bean.isPresent()) {
                    return bean.get();
                } else {
                    return defaultFactory.create(cls);
                }
            }
        });
        commandLine.setExecutionExceptionHandler((ex, commandLine1, parseResult) -> exceptionHandler.apply(ex, commandLine1));
        commandLine.setUsageHelpWidth(100);

        try {
            CodeGenConfig codeGenConfig = loadConfig();
            if (codeGenConfig != null) {
                beanContext.getBeanDefinitions(CodeGenCommand.class).stream()
                        .map(BeanDefinition::getBeanType)
                        .map(bt -> beanContext.createBean(bt, codeGenConfig))
                        .filter(CodeGenCommand::applies)
                        .forEach(commandLine::addSubcommand);
            }
        } catch (IOException e) {
        }

        return commandLine;
    }

    @Override
    public Integer call() throws Exception {
        throw new CommandLine.ParameterException(spec.commandLine(), "No command specified");
    }

    private static CodeGenConfig loadConfig() throws IOException {
        File micronautCli = new File("micronaut-cli.yml");
        if (micronautCli.exists()) {
            Yaml yaml = new Yaml();
            try (InputStream inputStream = Files.newInputStream(micronautCli.toPath())) {
                Map<String, Object> map = yaml.load(inputStream);
                BeanIntrospection<CodeGenConfig> introspection = BeanIntrospection.getIntrospection(CodeGenConfig.class);
                CodeGenConfig codeGenConfig = introspection.instantiate();
                introspection.getBeanProperties().forEach(bp -> {
                    bp.convertAndSet(codeGenConfig, map.get(bp.getName()));
                });
                return codeGenConfig;
            }
        }
        return null;
    }
}
