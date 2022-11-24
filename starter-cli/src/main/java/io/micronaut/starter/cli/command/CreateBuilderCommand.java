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
package io.micronaut.starter.cli.command;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine.Command;

import java.util.Collections;
import java.util.List;

import static picocli.CommandLine.Help.Ansi.AUTO;

@Command(name = CreateBuilderCommand.NAME, description = "A guided walk-through to create an application")
@Prototype
public class CreateBuilderCommand extends BuilderCommand {

    public static final String NAME = "create";

    private final ProjectGenerator projectGenerator;
    private final List<Feature> features;
    private final String prompt = AUTO.string("@|blue > |@");

    public CreateBuilderCommand(ProjectGenerator projectGenerator,
                                List<Feature> features) {
        this.projectGenerator = projectGenerator;
        this.features = features;
    }

    @Override
    public Integer call() throws Exception {
        AnsiConsole.systemInstall();
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(new DefaultParser())
                    .build();
            GenerateOptions generateOptions = createGenerateOptions(reader);
            List<String> applicationFeatures = getFeatures(generateOptions.getApplicationType(), terminal, features);
            Project project = getProject(reader);
            try (OutputHandler outputHandler = new FileSystemOutputHandler(project, false, this)) {
                projectGenerator.generate(generateOptions.getApplicationType(),
                        project,
                        generateOptions.getOptions(),
                        getOperatingSystem(),
                        applicationFeatures,
                        outputHandler,
                        this);
                out("@|blue ||@ Application created at " + outputHandler.getOutputLocation());
            }
        } catch (UserInterruptException | EndOfFileException e) {
            //no-op
        } finally {
            AnsiConsole.systemUninstall();
        }
        return 0;
    }

    public GenerateOptions createGenerateOptions(LineReader reader) {
        ApplicationType applicationType = getApplicationType(reader);
        Language language = getLanguage(reader);
        TestFramework testFramework = getTestFramework(reader, language);
        BuildTool buildTool = getBuildTool(reader, language);
        JdkVersion jdkVersion = getJdkVersion(reader);
        Options options = new Options(language, testFramework, buildTool, jdkVersion);
        return new GenerateOptions(applicationType, options, Collections.emptySet());
    }

    protected Language getLanguage(LineReader reader) {
        out("Choose your preferred language. (enter for default)");
        return getEnumOption(
                Language.class,
                language -> StringUtils.capitalize(language.getName()),
                Language.DEFAULT_OPTION,
                reader);
    }

    protected ApplicationType getApplicationType(LineReader reader) throws UserInterruptException, EndOfFileException {
        out("What type of application do you want to create? (enter for default)");
        return getEnumOption(
                ApplicationType.class,
                ApplicationType::getTitle,
                ApplicationType.DEFAULT_OPTION,
                reader);
    }
}
