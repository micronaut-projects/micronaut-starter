/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.langchain4j.languagemodels;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.langchain4j.Langchain4jLanguageModel;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.langchain4j.azure.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureLangchain4jLanguageModel implements Langchain4jLanguageModel {
    private static final String NAME = "langchain4j-azure";
    private static final String ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_AZURE = "micronaut-langchain4j-azure";
    private static final Dependency DEPENDENCY_MICRONAUT_LANGCHAIN4J_AZURE = MicronautDependencyUtils.langchain4j()
            .artifactId(ARTIFACT_ID_MICRONAUT_LANGCHAIN_4_J_AZURE)
            .compile()
            .build();

    @Override
    public String getTitle() {
        return "Azure Langchain4j";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addDependencies(GeneratorContext generatorContext) {
        Langchain4jLanguageModel.super.addDependencies(generatorContext);
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_LANGCHAIN4J_AZURE);
    }
}
