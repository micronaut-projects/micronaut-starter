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
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.BaseAvailableFeatures;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static picocli.CommandLine.Help.Ansi.AUTO;

@Command(name = CreateBuilderCommand.NAME, description = "A guided walk-through to create an application")
@Prototype
public class CreateBuilderCommand extends BaseCommand implements Callable<Integer> {

    public static final String NAME = "create";

    protected static final String PROMPT = AUTO.string("@|blue > |@");

    private final ProjectGenerator projectGenerator;
    private final List<Feature> features;

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
            ApplicationType applicationType = getApplicationType(reader);
            Language language = getLanguage(reader);
            TestFramework testFramework = getTestFramework(reader, language);
            BuildTool buildTool = getBuildTool(reader, language);
            JdkVersion jdkVersion = getJdkVersion(reader);
            List<String> applicationFeatures = getFeatures(applicationType, terminal);
            Project project = getProject(reader);

            try (OutputHandler outputHandler = new FileSystemOutputHandler(project, false, this)) {
                Options options = new Options(language, testFramework, buildTool, jdkVersion);
                projectGenerator.generate(applicationType, project, options, getOperatingSystem(), applicationFeatures, outputHandler, this);
                out("@|blue ||@ Application created at " + outputHandler.getOutputLocation());
            }
        } catch (UserInterruptException | EndOfFileException e) {
            //no-op
        } finally {
            AnsiConsole.systemUninstall();
        }
        return 0;
    }

    protected int getOption(LineReader reader, int max) throws UserInterruptException, EndOfFileException {
        String line;
        while (true) {
            line = reader.readLine(PROMPT);
            try {
                if (line == null || line.isEmpty()) {
                    return -1;
                }
                int choice = Integer.parseInt(line);
                if (choice > max || choice < 1) {
                    err("Invalid selection");
                } else {
                    return choice;
                }
            } catch (NumberFormatException e) {
                err("Invalid selection");
            }
        }
    }

    protected <T extends Enum<T>> T getEnumOption(Class<T> enumClass,
                                                  Function<T, String> titleFunc,
                                                  T defaultOption,
                                                  LineReader reader) throws UserInterruptException, EndOfFileException {
        T[] types = enumClass.getEnumConstants();
        for (T type: types) {
            out(AUTO.string("@|blue " + (type == defaultOption ? "*" : " ") + (type.ordinal() + 1) + ")|@ " + titleFunc.apply(type)));
        }
        int option = getOption(reader, types.length);
        out("");
        if (option == -1) {
            return defaultOption;
        }
        int choice = option - 1;
        return types[choice];
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

    protected TestFramework getTestFramework(LineReader reader, Language language) {
        out("Choose your preferred test framework. (enter for default)");
        return getEnumOption(
                TestFramework.class,
                TestFramework::getTitle,
                language.getDefaults().getTest(),
                reader
        );
    }

    protected BuildTool getBuildTool(LineReader reader, Language language) {
        out("Choose your preferred build tool. (enter for default)");
        return getEnumOption(
                BuildTool.class,
                BuildTool::getTitle,
                language.getDefaults().getBuild(),
                reader
        );
    }

    protected JdkVersion getJdkVersion(LineReader reader) {
        out("Choose the target JDK. (enter for default)");
        return getEnumOption(
                JdkVersion.class,
                jdkVersion -> Integer.toString(jdkVersion.majorVersion()),
                JdkVersion.DEFAULT_OPTION,
                reader
        );
    }

    protected List<String> getFeatures(ApplicationType applicationType, Terminal terminal) {
        AvailableFeatures availableFeatures = new BaseAvailableFeatures(features, applicationType);
        List<String> featureNames = availableFeatures.getFeatures().map(Feature::getName).collect(Collectors.toList());
        LineReader featuresReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter(featureNames))
                .parser(new DefaultParser())
                .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                .build();

        out("Enter any features to apply. Use tab for autocomplete and separate by a space.");
        while (true) {
            String featuresLine = featuresReader.readLine(PROMPT);
            if (StringUtils.trimToNull(featuresLine) == null) {
                out("");
                return new ArrayList<>();
            }
            List<String> selectedFeatures = Arrays.asList(featuresLine.split(" "));
            List<String> invalidFeatures = new ArrayList<>();
            for (String name: selectedFeatures) {
                if (!availableFeatures.findFeature(name).isPresent()) {
                    invalidFeatures.add(name);
                }
            }
            if (!invalidFeatures.isEmpty()) {
                err("The following features are not valid: " + invalidFeatures);
            } else {
                out("");
                return selectedFeatures;
            }
        }
    }

    protected Project getProject(LineReader reader) {
        out("Enter a name for the project.");
        while (true) {
            try {
                String name = reader.readLine(PROMPT);
                Project project = NameUtils.parse(name);
                out("");
                return project;
            } catch (IllegalArgumentException e) {
                err(e.getMessage());
            }
        }
    }
}
