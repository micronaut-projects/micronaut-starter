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
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;

@Requires(property = "micronaut.starter.feature.mcp.http.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class McpHttp implements McpFeature {
    public static final String NAME = "mcp-http";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MCP HTTP";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Model Context Protocol (MCP) using the http transport";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mcp/latest/guide/#server";
    }

    @Override
    public String getTransport() {
        return "HTTP";
    }
}
