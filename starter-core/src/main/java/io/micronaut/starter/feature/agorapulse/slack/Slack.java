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
package io.micronaut.starter.feature.agorapulse.slack;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerJava;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderJava;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.slackManifest;
import io.micronaut.starter.feature.test.Mockito;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.addMain;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.addTest;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.mainModel;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.testModel;

@Singleton
public class Slack implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-slack-http";

    private final Mockito mockito;

    public Slack(Mockito mockito) {
        this.mockito = mockito;
    }

    @Override
    @NonNull
    public String getCommunityFeatureName() {
        return "micronaut-slack";
    }

    @Override
    public String getCommunityFeatureTitle() {
        return "Micronaut Slack";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }

    @Override
    @Nullable
    public String getDescription() {
        return "Micronaut Slack is idiomatic alternative to Bolt Micronaut library for Slack integration into the Micronaut.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/micronaut-slack/";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Mockito.class)) {
            if (featureContext.getTestFramework() == TestFramework.JUNIT) {
                featureContext.addFeature(mockito);
            }
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
        addConfiguration(generatorContext);
        addExampleCode(generatorContext);
    }

    private void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID)
                .compile());
    }

    private void addConfiguration(GeneratorContext generatorContext) {
        Map<String, String> slack = new LinkedHashMap<>(1);
        slack.put("bot-token", "xoxb-" + UUID.randomUUID());
        slack.put("signing-secret", UUID.randomUUID().toString());

        Map<String, Object> nested = new LinkedHashMap<>(1);
        nested.put("slack", slack);

        generatorContext.getConfiguration().addNested(nested);
    }

    private void addExampleCode(GeneratorContext generatorContext) {
        senderModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "MessageSender", rockerModel, "messageSender")
        );

        senderTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "MessageSender", rockerModel, "messageSenderTest")
        );


        handlerModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "CommandHandler", rockerModel, "commandHandler")
        );

        handlerTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "CommandHandler", rockerModel, "commandHandlerTest")
        );

        addSlackManifest(generatorContext);
    }

    private void addSlackManifest(GeneratorContext generatorContext) {
        String subdomain = generatorContext.getProject().getPackageName().replace(".", "-");

        if (!subdomain.equals(generatorContext.getProject().getName())) {
            subdomain = subdomain + "-" + generatorContext.getProject().getName();
        }

        RockerModel manifestModel = slackManifest.template(generatorContext.getProject(), subdomain.toLowerCase(Locale.ROOT));
        generatorContext.addTemplate("SlackManifest", new RockerTemplate("slack-manifest.yml", manifestModel));
    }

    @NonNull
    private Optional<RockerModel> senderModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                messageSenderJava.template(generatorContext.getProject()),
                messageSenderGroovy.template(generatorContext.getProject()),
                messageSenderKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> senderTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                messageSenderTestJava.template(generatorContext.getProject()),
                messageSenderSpecGroovy.template(generatorContext.getProject()),
                messageSenderTestKotlin.template(generatorContext.getProject()),
                messageSenderTestKotest.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> handlerModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                commandHandlerJava.template(generatorContext.getProject()),
                commandHandlerGroovy.template(generatorContext.getProject()),
                commandHandlerKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> handlerTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                commandHandlerTestJava.template(generatorContext.getProject()),
                commandHandlerSpecGroovy.template(generatorContext.getProject()),
                commandHandlerTestKotlin.template(generatorContext.getProject()),
                commandHandlerTestKotest.template(generatorContext.getProject())
        );
    }

}
