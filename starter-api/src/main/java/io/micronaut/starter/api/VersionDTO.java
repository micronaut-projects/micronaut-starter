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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.starter.util.VersionInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Information about the application.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
@Schema(name = "Version")
public class VersionDTO extends Linkable {

    private final String serverURL;
    private final String path;

    /**
     * Default constructor.
     * @param configuration The configuration
     */
    public VersionDTO(StarterConfiguration configuration) {
        this.serverURL = configuration.getUrl().map(Object::toString).orElse(SocketUtils.LOCALHOST);
        this.path = configuration.getPath().orElse("/");
    }

    /**
     * @return The configured path
     */
    public String getPath() {
        return path;
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
    public String getMicronautVersion() {
        return VersionInfo.getMicronautVersion();
    }
}
