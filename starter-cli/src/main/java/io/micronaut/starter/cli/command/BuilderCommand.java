/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.core.util.StringUtils;
import io.micronaut.core.util.SupplierUtil;
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
import io.micronaut.starter.options.MicronautJdkVersionConfiguration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static picocli.CommandLine.Help.Ansi.AUTO;

/**
 * Abstract class to be extended by interactive commands.
 * @author Sergio del Amo
 * @since 3.8.0
 */
public abstract class BuilderCommand extends BaseCommand implements Callable<Integer> {

    // Wrapped in a memoized Supplier, so it's not created at build time
    public static final Supplier<String> PROMPT = SupplierUtil.memoized(() -> AUTO.string("@|blue > |@"));

    protected final ProjectGenerator projectGenerator;
    protected final List<Feature> features;

    protected BuilderCommand(ProjectGenerator projectGenerator, List<Feature> features) {
        this.projectGenerator = projectGenerator;
        this.features = features;
    }

    protected abstract GenerateOptions createGenerateOptions(LineReader reader);

    @Override
    public Integer call() throws Exception {
        AnsiConsole.systemInstall();
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(new DefaultParser())
                    .build();
            GenerateOptions options = createGenerateOptions(reader);
            // options.getFeatures() may be an unmodifiable set, so unpack it into a modifiable set
            Set<String> selectedFeatures = new HashSet<>(options.getFeatures());
            selectedFeatures.addAll(getFeatures(options.getApplicationType(), terminal, features));
            Project project = getProject(reader);
            try (OutputHandler outputHandler = new FileSystemOutputHandler(project, false, this)) {
                projectGenerator.generate(options.getApplicationType(),
                        project,
                        options.getOptions(),
                        getOperatingSystem(),
                        new ArrayList<>(selectedFeatures),
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

    protected List<String> getFeatures(ApplicationType applicationType, Terminal terminal, List<Feature> features) {
        AvailableFeatures availableFeatures = new BaseAvailableFeatures(features, applicationType);
        List<String> featureNames = availableFeatures.getFeatures().map(Feature::getName).toList();
        LineReader featuresReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter(featureNames))
                .parser(new DefaultParser())
                .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                .build();
        out("Enter any features to apply. Use tab for autocomplete and separate by a space.");
        while (true) {
            String featuresLine = featuresReader.readLine(PROMPT.get());
            if (StringUtils.trimToNull(featuresLine) == null) {
                out("");
                return new ArrayList<>();
            }
            List<String> selectedFeatures = Arrays.asList(featuresLine.split(" "));
            List<String> invalidFeatures = new ArrayList<>();
            for (String name: selectedFeatures) {
                if (availableFeatures.findFeature(name).isEmpty()) {
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
        List<String> candidates = getJdkVersionCandidates();
        JdkVersion defaultOption = MicronautJdkVersionConfiguration.DEFAULT_OPTION;
        if (candidates.size() == 1) {
            return defaultOption;
        }
        out("Choose the target JDK. (enter for default)");
        return JdkVersion.valueOf(Integer.parseInt(getListOption(
                candidates,
                s -> s,
                String.valueOf(defaultOption.majorVersion()),
                reader
        )));
    }

    protected List<String> getJdkVersionCandidates() {
        return new JdkVersionCandidates();
    }

    protected YesOrNo getYesOrNo(LineReader reader) {
        return getEnumOption(YesOrNo.class, yesOrNo -> StringUtils.capitalize(yesOrNo.name().toLowerCase()), YesOrNo.YES, reader);
    }

    protected int getOption(LineReader reader, int max) throws UserInterruptException, EndOfFileException {
        String line;
        while (true) {
            line = reader.readLine(PROMPT.get());
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

    protected <T extends Enum<T>> T getEnumOption(T[] types,
                                                  Function<T, String> titleFunc,
                                                  T defaultOption,
                                                  LineReader reader) throws UserInterruptException, EndOfFileException {
        for (int i = 0; i < types.length; i++) {
            T type = types[i];
            out(AUTO.string("@|blue " + (type == defaultOption ? "*" : " ") + (i + 1) + ")|@ " + titleFunc.apply(type)));
        }
        int option = getOption(reader, types.length);
        out("");
        if (option == -1) {
            return defaultOption;
        }
        int choice = option - 1;
        return types[choice];
    }

    protected String getListOption(List<String> types,
                                   Function<String, String> titleFunc,
                                   String defaultOption,
                                   LineReader reader) throws UserInterruptException, EndOfFileException {
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            out(AUTO.string("@|blue " + (type.equals(defaultOption) ? "*" : " ") + (i + 1) + ")|@ " + titleFunc.apply(type)));
        }
        int option = getOption(reader, types.size());
        out("");
        if (option == -1) {
            return defaultOption;
        }
        int choice = option - 1;
        return types.get(choice);
    }

    protected <T extends Enum<T>> T getEnumOption(Class<T> enumClass,
                                                  Function<T, String> titleFunc,
                                                  T defaultOption,
                                                  LineReader reader) throws UserInterruptException, EndOfFileException {
        T[] types = enumClass.getEnumConstants();
        return getEnumOption(types, titleFunc, defaultOption, reader);
    }

    protected Feature getFeatureOption(List<Feature> features,
                                       Function<Feature, String> titleFunc,
                                       Feature defaultOption,
                                       LineReader reader) throws UserInterruptException, EndOfFileException {
        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);
            out(AUTO.string("@|blue " + (feature.getName().equals(defaultOption.getName()) ? "*" : " ") + (i + 1) + ")|@ " + titleFunc.apply(feature)));
        }
        int option = getOption(reader, features.size());
        out("");
        if (option == -1) {
            return defaultOption;
        }
        int choice = option - 1;
        return features.get(choice);
    }

    protected Project getProject(LineReader reader) {
        out("Enter a name for the project.");
        while (true) {
            try {
                String name = reader.readLine(PROMPT.get());
                Project project = NameUtils.parse(name);
                out("");
                return project;
            } catch (IllegalArgumentException e) {
                err(e.getMessage());
            }
        }
    }

    protected Options getOptions(LineReader reader) {
        Language language = getLanguage(reader);
        TestFramework testFramework = getTestFramework(reader, language);
        BuildTool buildTool = getBuildTool(reader, language);
        JdkVersion jdkVersion = getJdkVersion(reader);
        return new Options(language, testFramework, buildTool, jdkVersion);
    }

    protected Language getLanguage(LineReader reader) {
        out("Choose your preferred language. (enter for default)");
        return getEnumOption(
                Language.class,
                language -> StringUtils.capitalize(language.getName()),
                Language.DEFAULT_OPTION,
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
}
