/*
 * Copyright 2021 original authors
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
package io.micronaut.starter.feature.config;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Models application environment configuration to specify where the configuration is rooted
 * for the given configuration values (key/value pairs).
 *
 * @since 2.3.0
 */
public class EnvConfiguration {

    public static final String DEFAULT_MAIN_PATH = "src/main/resources/";
    public static final String DEFAULT_TEST_PATH = "src/test/resources/";

    private final String path;
    private final Map<String, Object> envConfiguration;

    /**
     * A configuration rooted at path, with the given map of configurations
     *
     * @param path where the configuration is rooted, e.g. src/main/resources
     * @param envConfiguration the configuration value
     */
    public EnvConfiguration(@NonNull String path, @NonNull Map<String, Object> envConfiguration) {
        this.path = path.endsWith("/") ? path : path + "/";
        this.envConfiguration = envConfiguration;
    }

    @NonNull public String getPath() {
        return path;
    }

    @NonNull public Map<String, Object> getEnvConfiguration() {
        return envConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnvConfiguration that = (EnvConfiguration) o;
        return path.equals(that.path) && envConfiguration.equals(that.envConfiguration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, envConfiguration);
    }
}
