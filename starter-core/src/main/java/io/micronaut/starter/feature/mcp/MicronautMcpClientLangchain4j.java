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
package io.micronaut.starter.feature.mcp;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;
import static io.micronaut.starter.feature.Category.MCP;

@Requires(property = "micronaut.starter.feature.mcp.client.langchain4j.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class MicronautMcpClientLangchain4j implements Feature {

    public static final String NAME = "micronaut-mcp-client-langchain4j";
    private static final String ARTIFACT_ID_MICRONAUT_MCP_CLIENT_LANGCHAIN4J = "micronaut-mcp-client-langchain4j";
    private static final Dependency MICRONAUT_MCP_CLIENT_LANGCHAIN4J_DEPENDENCY =
            MicronautDependencyUtils.mcpDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_MCP_CLIENT_LANGCHAIN4J)
                    .test()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut MCP Client LangChain4j";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Model Context Protocol (MCP) powered by LangChain4j";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mcp/latest/guide";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.langchain4j.dev/tutorials/mcp/#mcp-client";
    }

    @Override
    public String getCategory() {
        return MCP;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_MCP_CLIENT_LANGCHAIN4J_DEPENDENCY);
    }
}
