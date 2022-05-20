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
package io.micronaut.starter.feature.agorapulse.worker;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobSpecGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobTestJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobTestKotest;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobTestKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestServiceGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestServiceJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestServiceKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobSpecGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestKotest;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.fallbackEmailDigestServiceGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.fallbackEmailDigestServiceJava;
import io.micronaut.starter.feature.agorapulse.worker.template.fallbackEmailDigestServiceKotlin;
import io.micronaut.starter.feature.test.Awaitility;
import io.micronaut.starter.feature.test.Mockito;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;


@Singleton
public class Worker implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-worker";

    private final Awaitility awaitility;

    private final Mockito mockito;

    public Worker(Awaitility awaitility, Mockito mockito) {
        this.awaitility = awaitility;
        this.mockito = mockito;
    }

    @Override
    @NonNull
    public String getName() {
        return "micronaut-worker";
    }

    @Override
    public String getTitle() {
        return "Micronaut Worker";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.SCHEDULING;
    }

    @Override
    @Nullable
    public String getDescription() {
        return "Micronaut Worker library provides advanced distributed scheduling capabilities for Micronaut";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/micronaut-worker/";
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
        if (!featureContext.isPresent(Awaitility.class)) {
            if (featureContext.getLanguage() == Language.JAVA || featureContext.getLanguage() == Language.KOTLIN) {
                featureContext.addFeature(awaitility);
            }
        }
        if (!featureContext.isPresent(Mockito.class)) {
            if (featureContext.getTestFramework() == TestFramework.JUNIT) {
                featureContext.addFeature(mockito);
            }
        }
    }

    private void addExampleCode(GeneratorContext generatorContext) {
        serviceModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "EmailDigestService", rockerModel, "emailDigestService")
        );

        fallbackServiceModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "FallbackEmailDigestService", rockerModel, "fallbackEmailDigestService")
        );

        simpleJobModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "EmailDigestSimpleJob", rockerModel, "emailDigestSimpleJob")
        );

        simpleJobTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "EmailDigestSimpleJob", rockerModel, "emailDigestSimpleJobTest")
        );

        distributedJobModel(generatorContext).ifPresent(rockerModel ->
                addMain(generatorContext, "EmailDigestDistributedJob", rockerModel, "emailDigestDistributedJob")
        );

        distributedJobTestModel(generatorContext).ifPresent(rockerModel ->
                addTest(generatorContext, "EmailDigestDistributedJob", rockerModel, "emailDigestDistributedJobTest")
        );
    }

    private void addMain(GeneratorContext generatorContext, String className, RockerModel rockerModel, String templateName) {
        String extension = generatorContext.getLanguage().getExtension();
        String srcDir = generatorContext.getLanguage().getSrcDir();
        generatorContext.addTemplate(
                templateName,
                new RockerTemplate(srcDir + "/{packagePath}/" + className + "." + extension, rockerModel)
        );
    }

    private void addTest(GeneratorContext generatorContext, String className, RockerModel rockerModel, String templateName) {
        String testSrcDir = generatorContext.getLanguage().getTestSrcDir();
        String extension = generatorContext.getLanguage().getExtension();
        String testFrameworkSuffix = generatorContext.getTestFramework().getTestFrameworkSuffix();
        generatorContext.addTemplate(
                templateName,
                new RockerTemplate(testSrcDir + "/{packagePath}/" + className + testFrameworkSuffix + extension, rockerModel)
        );
    }

    @NonNull
    private Optional<RockerModel> simpleJobModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                emailDigestSimpleJobJava.template(generatorContext.getProject()),
                emailDigestSimpleJobGroovy.template(generatorContext.getProject()),
                emailDigestSimpleJobKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> serviceModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                emailDigestServiceJava.template(generatorContext.getProject()),
                emailDigestServiceGroovy.template(generatorContext.getProject()),
                emailDigestServiceKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> fallbackServiceModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                fallbackEmailDigestServiceJava.template(generatorContext.getProject()),
                fallbackEmailDigestServiceGroovy.template(generatorContext.getProject()),
                fallbackEmailDigestServiceKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> simpleJobTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                emailDigestSimpleJobTestJava.template(generatorContext.getProject()),
                emailDigestSimpleJobSpecGroovy.template(generatorContext.getProject()),
                emailDigestSimpleJobTestKotlin.template(generatorContext.getProject()),
                emailDigestSimpleJobTestKotest.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> distributedJobModel(GeneratorContext generatorContext) {
        return mainModel(
                generatorContext,
                emailDigestDistributedJobJava.template(generatorContext.getProject()),
                emailDigestDistributedJobGroovy.template(generatorContext.getProject()),
                emailDigestDistributedJobKotlin.template(generatorContext.getProject())
        );
    }

    @NonNull
    private Optional<RockerModel> distributedJobTestModel(GeneratorContext generatorContext) {
        return testModel(
                generatorContext,
                emailDigestDistributedJobTestJava.template(generatorContext.getProject()),
                emailDigestDistributedJobSpecGroovy.template(generatorContext.getProject()),
                emailDigestDistributedJobTestKotlin.template(generatorContext.getProject()),
                emailDigestDistributedJobTestKotest.template(generatorContext.getProject())
        );
    }

    private Optional<RockerModel> mainModel(
            GeneratorContext generatorContext,
            @Nullable RockerModel javaModel,
            @Nullable RockerModel groovyModel,
            @Nullable RockerModel kotlinModel
    ) {
        switch (generatorContext.getLanguage()) {
            case JAVA:
                return Optional.ofNullable(javaModel);
            case GROOVY:
                return Optional.ofNullable(groovyModel);
            case KOTLIN:
                return Optional.ofNullable(kotlinModel);
            default:
                return Optional.empty();
        }
    }

    @NonNull
    private Optional<RockerModel> testModel(
            GeneratorContext generatorContext,
            @Nullable RockerModel javaModel,
            @Nullable RockerModel groovyModel,
            @Nullable RockerModel kotlinJUnitModel,
            @Nullable RockerModel kotestModel
    ) {
        if (generatorContext.getLanguage() == Language.JAVA && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.ofNullable(javaModel);
        }

        if (generatorContext.getLanguage() == Language.GROOVY && generatorContext.getTestFramework() == TestFramework.SPOCK) {
            return Optional.ofNullable(groovyModel);
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.ofNullable(kotlinJUnitModel);
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.KOTEST) {
            return Optional.ofNullable(kotestModel);
        }
        return Optional.empty();
    }
}
