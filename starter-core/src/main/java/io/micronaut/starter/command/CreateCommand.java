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
package io.micronaut.starter.command;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Options;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class CreateCommand extends BaseCommand implements Callable<Integer> {

    protected final AvailableFeatures availableFeatures;
    protected final FeatureValidator featureValidator;

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
    private final MicronautCommand command;

    public CreateCommand(AvailableFeatures availableFeatures,
                         FeatureValidator featureValidator,
                         ContextFactory contextFactory,
                         MicronautCommand command) {
        this.availableFeatures = availableFeatures;
        this.featureValidator = featureValidator;
        this.contextFactory = contextFactory;
        this.command = command;
    }

    protected abstract List<String> getSelectedFeatures();

    @Override
    public Integer call() throws Exception {

        if (listFeatures) {
            new ListFeatures(availableFeatures, new Options(lang, test, build), command, contextFactory).output(this);
            return 0;
        }

        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Please specify a name for the project");
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
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, getSelectedFeatures(), command, lang, build, test);
        CommandContext commandContext = contextFactory.createCommandContext(project, featureContext, this);

        commandContext.getConfiguration().put("micronaut.application.name", project.getName());
        commandContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(commandContext.getLanguage(),
                                commandContext.getTestFramework(),
                                commandContext.getProject(),
                                commandContext.getFeatures(),
                                commandContext.getCommand())));

        commandContext.applyFeatures();

        TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler);

        for (Template template: commandContext.getTemplates().values()) {
            RenderResult renderResult = templateRenderer.render(template);
            if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        templateRenderer.close();
    }
}
