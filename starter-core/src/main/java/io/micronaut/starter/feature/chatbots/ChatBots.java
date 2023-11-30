/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.chatbots;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.chatbots.template.about;
import io.micronaut.starter.feature.chatbots.template.aboutCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.template.aboutCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.template.aboutCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.template.azureReadme;
import io.micronaut.starter.feature.chatbots.template.finalCommandHandlerGroovy;
import io.micronaut.starter.feature.chatbots.template.finalCommandHandlerJava;
import io.micronaut.starter.feature.chatbots.template.finalCommandHandlerKotlin;
import io.micronaut.starter.feature.chatbots.template.telegramReadme;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.feature.validator.ValidationFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

/**
 * Base class for chatbot features.
 * <p>
 * Used to template out the common code between the chatbot features.
 *
 * @author Tim Yates
 * @since 4.3.0
 */
public abstract class ChatBots implements ChatBotsFeature {

    private final MicronautValidationFeature validationFeature;

    protected ChatBots(MicronautValidationFeature validationFeature) {
        this.validationFeature = validationFeature;
    }

    protected void renderTemplates(GeneratorContext generatorContext) {
        generatorContext.addTemplate(
                "about-markdown",
                new RockerTemplate("src/main/resources/botcommands/about.md", about.template(getChatBotType()))
        );
        generatorContext.addTemplate(
                "about-command-handler",
                generatorContext.getSourcePath("/{packagePath}/AboutCommandHandler"),
                aboutCommandHandlerJava.template(generatorContext.getProject()),
                aboutCommandHandlerKotlin.template(generatorContext.getProject()),
                aboutCommandHandlerGroovy.template(generatorContext.getProject())
        );

        generatorContext.addTemplate(
                "final-command-handler",
                generatorContext.getSourcePath("/{packagePath}/FinalCommandHandler"),
                finalCommandHandlerJava.template(generatorContext.getProject()),
                finalCommandHandlerKotlin.template(generatorContext.getProject()),
                finalCommandHandlerGroovy.template(generatorContext.getProject())
        );
        generatorContext.addHelpTemplate(new RockerWritable(telegramReadme.template(
                azureReadme.class.getName().replace(".", "/") + ".rocker.raw",
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                getBuildCommand(generatorContext.getBuildTool()))
        ));
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        addConfigurations(generatorContext);
        renderTemplates(generatorContext);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(ValidationFeature.class, validationFeature);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/";
    }

    protected abstract void addDependencies(GeneratorContext generatorContext);

    protected abstract void addConfigurations(GeneratorContext generatorContext);

    protected abstract String getChatBotType();

    protected abstract String rootReadMeTemplate();

    protected abstract String getBuildCommand(BuildTool buildTool);
}
