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

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
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

    private final MicronautCommand command;

    public CreateCommand(AvailableFeatures availableFeatures,
                         FeatureValidator featureValidator,
                         MicronautCommand command) {
        this.availableFeatures = availableFeatures;
        this.featureValidator = featureValidator;
        this.command = command;
    }

    protected abstract List<String> getSelectedFeatures();

    @Override
    public Integer call() throws Exception {
        Project project = NameUtils.parse(name);

        OutputHandler outputHandler = new FileSystemOutputHandler(project, inplace, this);

        generate(project, outputHandler);

        out("created " + name);
        return 0;
    }

    public void generate(OutputHandler outputHandler) throws IOException {
        generate(NameUtils.parse(name), outputHandler);
    }

    public void generate(Project project, OutputHandler outputHandler) throws IOException {
        final List<Feature> features = new ArrayList<>(8);
        for (String name: getSelectedFeatures()) {
            Feature feature = availableFeatures.findFeature(name).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        availableFeatures.getFeatures()
                .filter(f -> f instanceof DefaultFeature)
                .filter(f -> ((DefaultFeature) f).shouldApply(command, lang, features))
                .forEach(features::add);

        featureValidator.validate(lang, features);

        FeatureContext featureContext = new FeatureContext(lang, test, build, command, availableFeatures, features);

        featureContext.processSelectedFeatures();

        List<Feature> featureList = featureContext.getFinalFeatures(this);

        featureValidator.validate(lang, featureList);

        CommandContext commandContext = new CommandContext(featureContext, project);
        commandContext.getConfiguration().put("micronaut.application.name", project.getName());
        commandContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(commandContext.getLanguage(),
                                commandContext.getTestFramework(),
                                commandContext.getProject(),
                                commandContext.getFeatures(),
                                commandContext.getCommand())));

        for (Feature feature: featureList) {
            feature.apply(commandContext);
        }

        TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler);

        for (Template template: commandContext.getTemplates().values()) {
            templateRenderer.render(template);
        }

        templateRenderer.close();
    }
}
