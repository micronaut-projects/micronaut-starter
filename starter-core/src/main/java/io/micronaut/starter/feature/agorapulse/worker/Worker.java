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
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobTestKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestDistributedJobTestKotest;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobSpecGroovy;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestJava;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestKotlin;
import io.micronaut.starter.feature.agorapulse.worker.template.emailDigestSimpleJobTestKotest;
import io.micronaut.starter.feature.test.Awaitility;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;


@Singleton
public class Worker implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-worker";

    private final Awaitility awaitility;

    public Worker(Awaitility awaitility) {
        this.awaitility = awaitility;
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
        return Category.OTHER;
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
    }

    private void addExampleCode(GeneratorContext generatorContext) {
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
        if (generatorContext.getLanguage() == Language.JAVA) {
            return Optional.of(emailDigestSimpleJobJava.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(emailDigestSimpleJobGroovy.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(emailDigestSimpleJobKotlin.template(generatorContext.getProject()));
        }

        return Optional.empty();
    }

    @NonNull
    private Optional<RockerModel> simpleJobTestModel(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.JAVA && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.of(emailDigestSimpleJobTestJava.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.GROOVY && generatorContext.getTestFramework() == TestFramework.SPOCK) {
            return Optional.of(emailDigestSimpleJobSpecGroovy.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.of(emailDigestSimpleJobTestKotlin.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.KOTEST) {
            return Optional.of(emailDigestSimpleJobTestKotest.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }


    @NonNull
    private Optional<RockerModel> distributedJobModel(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.JAVA) {
            return Optional.of(emailDigestDistributedJobJava.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(emailDigestDistributedJobGroovy.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(emailDigestDistributedJobKotlin.template(generatorContext.getProject()));
        }

        return Optional.empty();
    }

    @NonNull
    private Optional<RockerModel> distributedJobTestModel(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.JAVA && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.of(emailDigestDistributedJobTestJava.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.GROOVY && generatorContext.getTestFramework() == TestFramework.SPOCK) {
            return Optional.of(emailDigestDistributedJobSpecGroovy.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            return Optional.of(emailDigestDistributedJobTestKotlin.template(generatorContext.getProject()));
        }

        if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.KOTEST) {
            return Optional.of(emailDigestDistributedJobTestKotest.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }

}
