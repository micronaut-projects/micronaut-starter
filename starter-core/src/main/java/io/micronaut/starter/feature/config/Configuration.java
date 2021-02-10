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

import io.micronaut.core.annotation.NonNull;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Models application environment configuration to specify where the configuration is rooted
 * for the given configuration values (key/value pairs).
 *
 * @since 2.3.0
 */
public class Configuration extends LinkedHashMap<String, Object> {

    private final String path;
    private final String fileName;
    private final String templateKey;

    /**
     * A configuration rooted at path, with the given map of configurations
     *
     * @param sourceSet where the configuration is rooted, e.g. main, test
     */
    public Configuration(@NonNull String sourceSet, @NonNull String fileName, @NonNull String templateKey) {
        super();
        this.path = "src/" + sourceSet + "/resources/";
        this.fileName = fileName;
        this.templateKey = templateKey;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    @NonNull
    public String getFullPath(String extension) {
        return path + fileName + "." + extension;
    }

    @NonNull
    public String getTemplateKey() {
        return templateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Configuration that = (Configuration) o;
        return templateKey.equals(that.templateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateKey);
    }

}
