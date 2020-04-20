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
package io.micronaut.starter.cli.command;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.*;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import io.micronaut.starter.util.VersionInfo;
import picocli.CommandLine;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CreateCommand extends BaseCommand implements Callable<Integer> {

    protected final AvailableFeatures availableFeatures;

    @CommandLine.Parameters(arity = "0..1", paramLabel = "NAME", description = "The name of the application to create.")
    String name;

    @CommandLine.Option(names = {"-l", "--lang"}, paramLabel = "LANG", description = "Which language to use. Possible values: ${COMPLETION-CANDIDATES}.")
    Language lang;

    @CommandLine.Option(names = {"-t", "--test"}, paramLabel = "TEST", description = "Which test framework to use. Possible values: ${COMPLETION-CANDIDATES}.")
    TestFramework test;

    @CommandLine.Option(names = {"-b", "--build"}, paramLabel = "BUILD-TOOL", description = "Which build tool to configure. Possible values: ${COMPLETION-CANDIDATES}.")
    BuildTool build = BuildTool.gradle;

    @CommandLine.Option(names = {"-i", "--inplace"}, description = "Create a service using the current directory")
    boolean inplace;

    @CommandLine.Option(names = {"--list-features"}, description = "Output the available features and their descriptions")
    boolean listFeatures;

    private final ContextFactory contextFactory;
    private final ApplicationType applicationType;
    private final ProjectGenerator projectGenerator;

    public CreateCommand(AvailableFeatures availableFeatures,
                         ContextFactory contextFactory,
                         ApplicationType applicationType,
                         ProjectGenerator projectGenerator) {
        this.availableFeatures = availableFeatures;
        this.contextFactory = contextFactory;
        this.applicationType = applicationType;
        this.projectGenerator = projectGenerator;
    }

    /**
     * @return The selected features.
     */
    protected abstract @Nonnull List<String> getSelectedFeatures();

    protected Map<String, Object> getAdditionalOptions() {
        return Collections.emptyMap();
    }

    @Override
    public Integer call() throws Exception {

        if (listFeatures) {
            new ListFeatures(availableFeatures, new Options(lang, test, build, VersionInfo.getJavaVersion()), applicationType, contextFactory).output(this);
            return 0;
        }

        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Specify an application name or use --inplace to create an application in the current directory");
        }

        Project project = NameUtils.parse(name);

        OutputHandler outputHandler = new FileSystemOutputHandler(project, inplace, this);

        generate(project, outputHandler);

        out("@|blue ||@ Application created at " + outputHandler.getOutputLocation());
        return 0;
    }

    public void generate(OutputHandler outputHandler) throws Exception {
        generate(NameUtils.parse(name), outputHandler);
    }

    public void generate(Project project, OutputHandler outputHandler) throws Exception {
        Options options = new Options(lang, test, build, VersionInfo.getJavaVersion(), getAdditionalOptions());

        projectGenerator.generate(applicationType, project, options, getSelectedFeatures(), outputHandler, this);
    }
}
