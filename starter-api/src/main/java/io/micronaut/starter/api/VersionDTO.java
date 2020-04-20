/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.version.VersionUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Information about the application.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
@Schema(name = "Version")
public class VersionDTO {

    private final String serverURL;
    private final InetSocketAddress serverAddress;
    private final Map<String, String> metadata;

    /**
     * Default constructor.
     * @param serverURL The server URL
     * @param serverAddress The server address
     * @param metadata The metadata
     */
    public VersionDTO(String serverURL, InetSocketAddress serverAddress, Map<String, String> metadata) {
        this.serverURL = serverURL;
        this.serverAddress = serverAddress;
        this.metadata = metadata;
    }

    /**
     * @return The metadata
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * @return The server address
     */
    public String getServerHost() {
        String host = System.getenv(Environment.HOSTNAME);
        return host != null ? host : serverAddress.getHostName();
    }

    /**
     * @return The server URL
     */
    public String getServerURL() {
        return serverURL;
    }

    /**
     * @return The version
     */
    public String getVersion() {
        return VersionUtils.MICRONAUT_VERSION;
    }
}
