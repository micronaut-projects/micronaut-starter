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
package io.micronaut.starter.application.generator;

import io.micronaut.context.BeanContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.starter.application.ContextFactory;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.cli;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Singleton
public class ProjectGenerator {

    private final ContextFactory contextFactory;
    private final BeanContext beanContext;

    public ProjectGenerator(ContextFactory contextFactory, BeanContext beanContext) {
        this.contextFactory = contextFactory;
        this.beanContext = beanContext;
    }

    public void generate(ApplicationType applicationType,
                         Project project,
                         Options options,
                         List<String> selectedFeatures,
                         OutputHandler outputHandler,
                         ConsoleOutput consoleOutput) throws Exception {

        GeneratorContext generatorContext = createGeneratorContext(applicationType, project, options, selectedFeatures, consoleOutput);

        generate(applicationType, project, outputHandler, generatorContext);
    }

    public void generate(ApplicationType applicationType, Project project, OutputHandler outputHandler, GeneratorContext generatorContext) throws Exception {
        List<String> features = new ArrayList<>(generatorContext.getFeatures().size());
        features.addAll(generatorContext.getFeatures());
        features.sort(Comparator.comparing(Function.identity()));

        generatorContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(generatorContext.getLanguage(),
                                generatorContext.getTestFramework(),
                                generatorContext.getBuildTool(),
                                generatorContext.getProject(),
                                features,
                                applicationType)));

        generatorContext.applyFeatures();

        try (TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler)) {
            for (Template template: generatorContext.getTemplates().values()) {
                RenderResult renderResult = templateRenderer.render(template);
                if (renderResult.getError() != null) {
                    throw renderResult.getError();
                }
            }
        }
    }

    public GeneratorContext createGeneratorContext(ApplicationType applicationType, Project project, Options options, List<String> selectedFeatures, ConsoleOutput consoleOutput) {
        AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures.class, Qualifiers.byName(applicationType.getName()));

        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, selectedFeatures, applicationType, options);
        return contextFactory.createGeneratorContext(project, featureContext, consoleOutput);
    }

}
