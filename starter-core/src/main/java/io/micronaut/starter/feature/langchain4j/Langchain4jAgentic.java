/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.starter.feature.langchain4j;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.langchain4j.agentic.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Langchain4jAgentic implements Langchain4jFeature {

    public static final String NAME = "langchain4j-agentic";
    private static final String ARTIFACT_ID_LANGCHAIN4J_AGENTIC = "micronaut-langchain4j-agentic";
    private static final Dependency LANGCHAIN4J_AGENTIC_DEPENDENCY =
            MicronautDependencyUtils.langchain4j()
                    .artifactId(ARTIFACT_ID_LANGCHAIN4J_AGENTIC)
                    .test()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut LangChain4j Agentic";
    }

    @Override
    public String getDescription() {
        return "Enables the declaration of LangChain4j Agentic services";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mcp/latest/guide/#agenticService";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.langchain4j.dev/tutorials/agents";
    }

    @Override
    public String getCategory() {
        return Category.AI;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(LANGCHAIN4J_AGENTIC_DEPENDENCY);
    }
}
