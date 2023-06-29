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
package io.micronaut.starter.feature.agorapulse;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.CommunityFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import java.util.Optional;

public interface AgoraPulseFeature extends CommunityFeature {

    @Override
    default String getThirdPartyDocumentation() {
        return "https://agorapulse.github.io/agorapulse-oss/#_micronaut_libraries";
    }

    /**
     * @return Indicates name of the community contributor.
     */
    @Override
    @NonNull
    default String getCommunityContributor() {
        return "Agorapulse";
    }

    static Optional<RockerModel> mainModel(
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
    static Optional<RockerModel> testModel(
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

    static void addMain(GeneratorContext generatorContext, String className, RockerModel rockerModel, String templateName) {
        String extension = generatorContext.getLanguage().getExtension();
        String srcDir = generatorContext.getLanguage().getSrcDir();
        generatorContext.addTemplate(
                templateName,
                new RockerTemplate(srcDir + "/{packagePath}/" + className + "." + extension, rockerModel)
        );
    }

    static void addTest(GeneratorContext generatorContext, String className, RockerModel rockerModel, String templateName) {
        String testSrcDir = generatorContext.getLanguage().getTestSrcDir();
        String extension = generatorContext.getLanguage().getExtension();
        String testFrameworkSuffix = generatorContext.getTestFramework().getTestFrameworkSuffix();
        generatorContext.addTemplate(
                templateName,
                new RockerTemplate(testSrcDir + "/{packagePath}/" + className + testFrameworkSuffix + extension, rockerModel)
        );
    }

    static void addTestUtil(GeneratorContext generatorContext, String className, RockerModel rockerModel, String templateName) {
        String testSrcDir = generatorContext.getLanguage().getTestSrcDir();
        String extension = generatorContext.getLanguage().getExtension();
        generatorContext.addTemplate(
                templateName,
                new RockerTemplate(testSrcDir + "/{packagePath}/" + className +  '.' + extension, rockerModel)
        );
    }

}
