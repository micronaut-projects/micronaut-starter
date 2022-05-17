/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.feature.function.CloudProvider;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import io.micronaut.starter.util.VersionInfo;
import picocli.CommandLine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CreateCommand extends BaseCommand implements Callable<Integer> {

    protected final AvailableFeatures availableFeatures;

    @ReflectiveAccess
    @CommandLine.Parameters(arity = "0..1", paramLabel = "NAME", description = "The name of the application to create.")
    String name;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-l", "--lang"}, paramLabel = "LANG", description = "Which language to use. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = LanguageCandidates.class, converter = LanguageConverter.class)
    Language lang;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-c", "--cloudprovider"}, paramLabel = "CLOUDPROVIDER", description = "Which cloud provider to use. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = CloudProviderCandidates.class, converter = CloudProviderConverter.class)
    CloudProvider cloudProvider;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-t", "--test"}, paramLabel = "TEST", description = "Which test framework to use. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = TestFrameworkCandidates.class, converter = TestFrameworkConverter.class)
    TestFramework test;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-b", "--build"}, paramLabel = "BUILD-TOOL", description = "Which build tool to configure. Possible values: ${COMPLETION-CANDIDATES}.", completionCandidates = BuildToolCandidates.class, converter = BuildToolConverter.class)
    BuildTool build;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-i", "--inplace"}, description = "Create a service using the current directory")
    boolean inplace;

    @ReflectiveAccess
    @CommandLine.Option(names = {"--list-features"}, description = "Output the available features and their descriptions")
    boolean listFeatures;

    @ReflectiveAccess
    @CommandLine.Option(names = {"--jdk", "--java-version"}, description = "The JDK version the project should target")
    Integer javaVersion;

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
    protected abstract @NonNull List<String> getSelectedFeatures();

    protected Map<String, Object> getAdditionalOptions() {
        return Collections.emptyMap();
    }

    @Override
    public Integer call() throws Exception {
        if (listFeatures) {
            new ListFeatures(availableFeatures,
                    new Options(lang, test, build, getJdkVersion(), cloudProvider),
                    applicationType,
                    getOperatingSystem(),
                    contextFactory).output(this);
            return 0;
        }
        Project project;
        try {
            project = NameUtils.parse(name);
        } catch (IllegalArgumentException e) {
            throw new CommandLine.ParameterException(this.spec.commandLine(), StringUtils.isEmpty(name) ? "Specify an application name or use --inplace to create an application in the current directory" : e.getMessage());
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
        Options options = new Options(lang, test, build, getJdkVersion(), cloudProvider, getAdditionalOptions());

        projectGenerator.generate(applicationType, project, options, getOperatingSystem(), getSelectedFeatures(), outputHandler, this);
    }

    private JdkVersion getJdkVersion() {
        if (javaVersion == null) {
            return VersionInfo.getJavaVersion();
        } else {
            return JdkVersion.valueOf(javaVersion);
        }
    }
}
