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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import io.micronaut.starter.util.VersionInfo;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CreateCommand extends BaseCommand implements Callable<Integer> {

    protected final AvailableFeatures availableFeatures;

    @ReflectiveAccess
    @Parameters(arity = "0..1", paramLabel = "NAME", description = "The name of the application to create.")
    protected String name;

    @ReflectiveAccess
    @Option(names = {"-l", "--lang"}, paramLabel = "LANG", description = "Which language to use. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = LanguageCandidates.class, converter = LanguageConverter.class)
    protected Language lang;

    @ReflectiveAccess
    @Option(names = {"-t", "--test"}, paramLabel = "TEST", description = "Which test framework to use. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = TestFrameworkCandidates.class, converter = TestFrameworkConverter.class)
    protected TestFramework test;

    @ReflectiveAccess
    @Option(names = {"-b", "--build"}, paramLabel = "BUILD-TOOL", description = "Which build tool to configure. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = BuildToolCandidates.class, converter = BuildToolConverter.class)
    protected BuildTool build;

    @ReflectiveAccess
    @Option(names = {"-i", "--inplace"}, description = "Create a service using the current directory")
    protected boolean inplace;

    @ReflectiveAccess
    @Option(names = {"--list-features"}, description = "Output the available features and their descriptions")
    protected boolean listFeatures;

    @ReflectiveAccess
    @Option(names = {"--jdk", "--java-version"}, description = "The JDK version the project should target")
    protected Integer javaVersion;

    protected final ContextFactory contextFactory;
    protected final ApplicationType applicationType;
    protected final ProjectGenerator projectGenerator;

    protected CreateCommand(AvailableFeatures availableFeatures,
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
    protected abstract @NonNull List<String> getSelectedFeatures();

    protected Map<String, Object> getAdditionalOptions() {
        return Collections.emptyMap();
    }

    @Override
    public Integer call() throws Exception {
        if (listFeatures) {
            new ListFeatures(availableFeatures,
                    new Options(lang, test, build, getJdkVersion()),
                    applicationType,
                    getOperatingSystem(),
                    contextFactory).output(this);
            return 0;
        }
        Project project;
        try {
            project = NameUtils.parse(name);
        } catch (IllegalArgumentException e) {
            throw new ParameterException(spec.commandLine(), StringUtils.isEmpty(name) ? "Specify an application name or use --inplace to create an application in the current directory" : e.getMessage());
        }

        OutputHandler outputHandler = new FileSystemOutputHandler(project, inplace, this);

        generate(project, outputHandler);

        out("@|blue ||@ Application created at " + outputHandler.getOutputLocation());
        return 0;
    }

    public void generate(OutputHandler outputHandler) throws Exception {
        generate(NameUtils.parse(name), outputHandler);
    }

    public void generate(Project project, OutputHandler outputHandler) throws Exception {
        Options options = new Options(lang, test, build, getJdkVersion(), getAdditionalOptions());

        projectGenerator.generate(applicationType, project, options, getOperatingSystem(), getSelectedFeatures(), outputHandler, this);
    }

    protected JdkVersion getJdkVersion() {
        return javaVersion == null ? VersionInfo.getJavaVersion() : JdkVersion.valueOf(javaVersion);
    }
}
