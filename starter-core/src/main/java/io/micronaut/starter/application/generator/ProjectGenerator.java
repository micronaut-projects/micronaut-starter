package io.micronaut.starter.application.generator;

import io.micronaut.context.BeanContext;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Options;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.ConsoleOutput;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.cli;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TemplateRenderer;

import javax.inject.Singleton;
import java.util.List;

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

        AvailableFeatures availableFeatures = beanContext.getBean(applicationType.getAvailableFeaturesClass());

        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, selectedFeatures, applicationType, options);
        GeneratorContext generatorContext = contextFactory.createGeneratorContext(project, featureContext, consoleOutput);

        generatorContext.addTemplate("micronautCli",
                new RockerTemplate("micronaut-cli.yml",
                        cli.template(generatorContext.getLanguage(),
                                generatorContext.getTestFramework(),
                                generatorContext.getBuildTool(),
                                generatorContext.getProject(),
                                generatorContext.getFeatures(),
                                applicationType)));

        generatorContext.applyFeatures();

        TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler);

        for (Template template: generatorContext.getTemplates().values()) {
            RenderResult renderResult = templateRenderer.render(template);
            if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        templateRenderer.close();
    }

}
