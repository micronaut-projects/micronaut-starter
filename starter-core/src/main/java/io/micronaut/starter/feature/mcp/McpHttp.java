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

@Requires(property = "micronaut.starter.feature.mcp.http.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class McpHttp implements Feature {

    public static final String NAME = "mcp-http";
    private static final String ARTIFACT_ID_MICRONAUT_MCP_HTTP = "micronaut-mcp-server-java-sdk";
    private static final Dependency MCP_HTTP_DEPENDENCY =
            MicronautDependencyUtils.mcpDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_MCP_HTTP)
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MCP Http";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Model Context Protocol (MCP) using the http transport";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mcp/latest/guide";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://modelcontextprotocol.io/specification/2025-06-18/basic/transports#streamable-http";
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
        generatorContext.getConfiguration().put("micronaut.mcp.server.info.name", "mcpdemo");
        generatorContext.getConfiguration().put("micronaut.mcp.server.info.version", "0.0.1");
        generatorContext.getConfiguration().put("micronaut.mcp.server.transport", "HTTP");
        generatorContext.addDependency(MCP_HTTP_DEPENDENCY);
    }
}
