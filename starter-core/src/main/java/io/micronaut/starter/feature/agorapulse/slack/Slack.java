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
import io.micronaut.starter.feature.agorapulse.gru.GruHttp;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerJava;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.commandHandlerTestKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.eventJson;
import io.micronaut.starter.feature.agorapulse.slack.template.gruSlackUtilGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.gruSlackUtilJava;
import io.micronaut.starter.feature.agorapulse.slack.template.gruSlackUtilKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderJava;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.messageSenderTestKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.methodsClientUtilGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.methodsClientUtilJava;
import io.micronaut.starter.feature.agorapulse.slack.template.methodsClientUtilKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.methodsClientUtilKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerGruSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerGruTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerGruTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerGruTestKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerJava;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerKotlin;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerSpecGroovy;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerTestJava;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerTestKotest;
import io.micronaut.starter.feature.agorapulse.slack.template.reactionHandlerTestKotlin;
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
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.addTestUtil;
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

        reactionModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "ReactionHandler", rockerModel, "reactionHandler")
        );

        reactionTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "ReactionHandler", rockerModel, "reactionHandlerTest")
        );

        handlerModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "CommandHandler", rockerModel, "commandHandler")
        );

        handlerTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "CommandHandler", rockerModel, "commandHandlerTest")
        );

        methodsClientUtilModel(generatorContext).ifPresent(rockerModel ->
                addTestUtil(generatorContext, "MethodsClientUtil", rockerModel, "methodsClientUtil")
        );

        addSlackManifest(generatorContext);

        if (generatorContext.isFeaturePresent(GruHttp.class)) {

            gruSlackUtilModel(generatorContext).ifPresent(rockerModel ->
                    addTestUtil(generatorContext, "GruSlackUtil", rockerModel, "gruSlackUtil")
            );

            reactionTestGruModel(generatorContext).ifPresent(rockerModel ->
                    addTest(generatorContext, "ReactionHandlerGru", rockerModel, "reactionHandlerGruTest")
            );

            addGruTestFixture(generatorContext, "ReactionHandlerGru");
        }
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
    private Optional<RockerModel> reactionModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                reactionHandlerJava.template(generatorContext.getProject()),
                reactionHandlerGroovy.template(generatorContext.getProject()),
                reactionHandlerKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> reactionTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                reactionHandlerTestJava.template(generatorContext.getProject()),
                reactionHandlerSpecGroovy.template(generatorContext.getProject()),
                reactionHandlerTestKotlin.template(generatorContext.getProject()),
                reactionHandlerTestKotest.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> methodsClientUtilModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                methodsClientUtilJava.template(generatorContext.getProject()),
                methodsClientUtilGroovy.template(generatorContext.getProject()),
                methodsClientUtilKotlin.template(generatorContext.getProject()),
                methodsClientUtilKotest.template(generatorContext.getProject())
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

    private Optional<RockerModel> gruSlackUtilModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                gruSlackUtilJava.template(generatorContext.getProject()),
                gruSlackUtilGroovy.template(generatorContext.getProject()),
                gruSlackUtilKotlin.template(generatorContext.getProject()),
                gruSlackUtilKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> reactionTestGruModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                reactionHandlerGruTestJava.template(generatorContext.getProject()),
                reactionHandlerGruSpecGroovy.template(generatorContext.getProject()),
                reactionHandlerGruTestKotlin.template(generatorContext.getProject()),
                reactionHandlerGruTestKotest.template(generatorContext.getProject())
        );
    }

    private void addGruTestFixture(GeneratorContext generatorContext, String className) {
        String suffix = generatorContext.getTestFramework().getTestFrameworkSuffixWithoutTrailingDot();
        generatorContext.addTemplate("eventJson",
                new RockerTemplate("src/test/resources/{packagePath}/" + className + suffix + "/event.json", eventJson.template()));
    }

}
