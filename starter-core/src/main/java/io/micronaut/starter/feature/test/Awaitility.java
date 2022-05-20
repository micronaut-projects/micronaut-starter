/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class Awaitility implements Feature {

    private static final String AWAITILITY_ARTIFACT_ID = "awaitility";
    private static final String AWAITILITY_GROOVY_ARTIFACT_ID = "awaitility-groovy";
    private static final String AWAITILITY_KOTLIN_ARTIFACT_ID = "awaitility-kotlin";

    @Override
    public String getName() {
        return "awaitility";
    }

    @Override
    public String getTitle() {
        return "Awaitility Framework";
    }

    @Override
    public String getDescription() {
        return "Awaitility is a framework for testing asynchronous code";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(getArtifactForLanguage(generatorContext.getLanguage()))
                .test());
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/awaitility/awaitility";
    }

    private String getArtifactForLanguage(Language language) {
        switch (language) {
            case JAVA: return AWAITILITY_ARTIFACT_ID;
            case GROOVY: return AWAITILITY_GROOVY_ARTIFACT_ID;
            case KOTLIN: return AWAITILITY_KOTLIN_ARTIFACT_ID;
            default: throw new IllegalArgumentException("Unsupported language " + language);
        }
    }
}
