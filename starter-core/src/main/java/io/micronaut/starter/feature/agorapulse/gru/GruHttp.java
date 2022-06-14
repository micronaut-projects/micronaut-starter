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
package io.micronaut.starter.feature.agorapulse.gru;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerKotlin;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerJava;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerGroovy;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerTestJava;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerTestGroovy;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerTestKotlin;
import io.micronaut.starter.feature.agorapulse.gru.template.helloWorldGruControllerTestKotest;
import io.micronaut.starter.feature.agorapulse.gru.template.gruIndexJson;

import java.util.Optional;


@Singleton
public class GruHttp implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "gru-micronaut";

    @Override
    @NonNull
    public String getCommunityFeatureName() {
        return "gru-http";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Gru HTTP - interaction testing";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext);
        addExampleCode(generatorContext);
    }

    private void addDependency(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID)
                .test());
    }

    private void addExampleCode(GeneratorContext generatorContext) {
        final String filename = "HelloWorldGruController";
        addController(generatorContext, filename);
        addControllerTest(generatorContext, filename);
        addTestFixture(generatorContext, filename);
    }

    private void addController(GeneratorContext generatorContext, String className) {
        String templateName = "helloWorldGruController";
        String extension = generatorContext.getLanguage().getExtension();
        String srcDir = generatorContext.getLanguage().getSrcDir();
        controllerModel(generatorContext).ifPresent(rockerModel ->
                generatorContext.addTemplate(templateName,
                    new RockerTemplate(srcDir + "/{packagePath}/" + className + "." + extension, rockerModel)));
    }

    @NonNull
    private Optional<RockerModel> controllerModel(GeneratorContext generatorContext) {
        RockerModel rockerModel = null;
        if (generatorContext.getLanguage() == Language.JAVA) {
            rockerModel = helloWorldGruControllerJava.template(generatorContext.getProject());
        } else if (generatorContext.getLanguage() == Language.GROOVY) {
            rockerModel = helloWorldGruControllerGroovy.template(generatorContext.getProject());
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            rockerModel = helloWorldGruControllerKotlin.template(generatorContext.getProject());
        }
        return Optional.ofNullable(rockerModel);
    }

    @NonNull
    private Optional<RockerModel> controllerTestRockerModel(GeneratorContext generatorContext) {
        RockerModel rockerModel = null;
        if (generatorContext.getLanguage() == Language.JAVA && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            rockerModel = helloWorldGruControllerTestJava.template(generatorContext.getProject());
        } else if (generatorContext.getLanguage() == Language.GROOVY && generatorContext.getTestFramework() == TestFramework.SPOCK) {
            rockerModel = helloWorldGruControllerTestGroovy.template(generatorContext.getProject());
        } else if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            rockerModel = helloWorldGruControllerTestKotlin.template(generatorContext.getProject());
        } else if (generatorContext.getLanguage() == Language.KOTLIN && generatorContext.getTestFramework() == TestFramework.KOTEST) {
            rockerModel = helloWorldGruControllerTestKotest.template(generatorContext.getProject());
        }
        return Optional.ofNullable(rockerModel);
    }

    private void addControllerTest(GeneratorContext generatorContext, String className) {
        final String templateName = "helloWorldGruControllerTest";
        String testSrcDir = generatorContext.getLanguage().getTestSrcDir();
        String extension = generatorContext.getLanguage().getExtension();
        String testFrameworkSuffix = generatorContext.getTestFramework().getTestFrameworkSuffix();
        controllerTestRockerModel(generatorContext).ifPresent(rockerModel ->
                generatorContext.addTemplate(templateName,
                        new RockerTemplate(testSrcDir + "/{packagePath}/" + className + testFrameworkSuffix + extension, rockerModel)));
    }

    private void addTestFixture(GeneratorContext generatorContext, String className) {
        String suffix = generatorContext.getTestFramework().getTestFrameworkSuffixWithoutTrailingDot();
        generatorContext.addTemplate("gruIndexJson",
                new RockerTemplate("src/test/resources/{packagePath}/" + className + suffix + "/gruIndex.json", gruIndexJson.template()));
    }

    @Override
    @Nullable
    public String getDescription() {
        return "Gru is HTTP interaction testing framework with out-of-box support for Micronaut";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/gru/";
    }
}
