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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;

import static io.micronaut.starter.feature.Category.MCP;

public interface McpFeature extends Feature {
    String ARTIFACT_ID_MICRONAUT_MCP_SERVER_JAVA_SDK = "micronaut-mcp-server-java-sdk";
    Dependency MCP_HTTP_DEPENDENCY =
            MicronautDependencyUtils.mcpDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_MCP_SERVER_JAVA_SDK)
                    .compile()
                    .build();

    @Nullable
    @Override
    default String getThirdPartyDocumentation() {
        return "https://modelcontextprotocol.io";
    }

    @Override
    default String getCategory() {
        return MCP;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.mcp.server.info.name", "mcpdemo");
        generatorContext.getConfiguration().put("micronaut.mcp.server.info.version", "0.0.1");
        generatorContext.getConfiguration().put("micronaut.mcp.server.transport", getTransport());
        generatorContext.addDependency(MCP_HTTP_DEPENDENCY);
    }

    @NonNull
    String getTransport();
}
