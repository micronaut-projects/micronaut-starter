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
package io.micronaut.starter.feature.agorapulse.permissions;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageGroovy;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageJava;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageKotlin;
import io.micronaut.starter.feature.agorapulse.permissions.template.messagePermissionAdvisorGroovy;
import io.micronaut.starter.feature.agorapulse.permissions.template.messagePermissionAdvisorJava;
import io.micronaut.starter.feature.agorapulse.permissions.template.messagePermissionAdvisorKotlin;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceGroovy;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceJava;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceKotlin;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceSpecGroovy;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceTestKotest;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceTestKotlin;
import io.micronaut.starter.feature.agorapulse.permissions.template.messageServiceTestJava;
import io.micronaut.starter.feature.security.Security;
import io.micronaut.starter.feature.test.Mockito;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.addMain;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.addTest;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.mainModel;
import static io.micronaut.starter.feature.agorapulse.AgoraPulseFeature.testModel;


@Singleton
public class Permissions implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-permissions";

    private final Mockito mockito;

    private final Security security;

    public Permissions(Mockito mockito, Security security) {
        this.mockito = mockito;
        this.security = security;
    }

    @Override
    @NonNull
    public String getCommunityFeatureName() {
        return "micronaut-permissions";
    }

    @Override
    public String getCommunityFeatureTitle() {
        return "Micronaut Permissions";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.SECURITY;
    }

    @Override
    @Nullable
    public String getDescription() {
        return "Micronaut Permissions is a lightweight library to declare object level permissions in Micronaut";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/micronaut-permissions/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
        addExampleCode(generatorContext);
    }

    private void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID)
                .compile());
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Security.class)) {
            featureContext.addFeature(security);
        }
        if (!featureContext.isPresent(Mockito.class)) {
            if (featureContext.getTestFramework() == TestFramework.JUNIT) {
                featureContext.addFeature(mockito);
            }
        }
    }

    private void addExampleCode(GeneratorContext generatorContext) {
        messageModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "Message", rockerModel, "message")
        );

        advisorModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "MessagePermissionAdvisor", rockerModel, "messagePermissionAdvisor")
        );

        serviceModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "MessageService", rockerModel, "messageService")
        );

        serviceTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "MessageService", rockerModel, "messageServiceTest")
        );
    }

    @NonNull
    private Optional<RockerModel> messageModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                messageJava.template(generatorContext.getProject()),
                messageGroovy.template(generatorContext.getProject()),
                messageKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> advisorModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                messagePermissionAdvisorJava.template(generatorContext.getProject()),
                messagePermissionAdvisorGroovy.template(generatorContext.getProject()),
                messagePermissionAdvisorKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> serviceModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                messageServiceJava.template(generatorContext.getProject()),
                messageServiceGroovy.template(generatorContext.getProject()),
                messageServiceKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> serviceTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                messageServiceTestJava.template(generatorContext.getProject()),
                messageServiceSpecGroovy.template(generatorContext.getProject()),
                messageServiceTestKotlin.template(generatorContext.getProject()),
                messageServiceTestKotest.template(generatorContext.getProject())
        );
    }

}
