/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.config;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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
     * @param fileName Filename
     * @param templateKey Template Key
     */
    public Configuration(@NonNull String sourceSet, @NonNull String fileName, @NonNull String templateKey) {
        super();
        this.path = "src/" + sourceSet + "/resources/";
        this.fileName = fileName;
        this.templateKey = templateKey;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key != null) {
            return super.containsKey(key) || containsNested(key.toString());
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key != null && containsKey(key)) {
            final Object o = super.get(key);
            if (o != null) {
                return o;
            } else {
                return getNested(key.toString());
            }
        }
        return null;
    }

    /**
     * Add a nested value for the given path.
     *
     * @param path The path
     * @param value The value
     * @return this configuration
     */
    public Configuration addNested(String path, Object value) {
        if (StringUtils.isNotEmpty(path)) {
            final String[] tokens = path.split("\\.");
            LinkedHashMap<String, Object> map = this;
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (i == tokens.length - 1) {
                    map.put(token, value);
                } else {
                    //noinspection unchecked
                    map = (LinkedHashMap<String, Object>) map.computeIfAbsent(token, (key) -> new LinkedHashMap<>(5));
                }
            }
        }
        return this;
    }

    /**
     * Add nested values for the given path.
     *
     * @param values A map of path to value entries
     * @return this configuration
     */
    public Configuration addNested(Map<String, Object> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            values.forEach(this::addNested);
        }
        return this;
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

    private boolean containsNested(String key) {
        if (key.indexOf('.') == -1) {
            return false;
        }
        final String[] tokens = key.split("\\.");
        Map<String, Object> map = this;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (!map.containsKey(token)) {
                return false;
            } else {
                final Object o = map.get(token);
                if (i == tokens.length - 1) {
                    return true;
                } else if (o instanceof Map) {
                    map = (Map<String, Object>) o;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private Object getNested(String key) {
        if (key.indexOf('.') == -1) {
            return null;
        }
        final String[] tokens = key.split("\\.");
        Map<String, Object> map = this;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (!map.containsKey(token)) {
                return null;
            } else {
                final Object o = map.get(token);
                if (i == tokens.length - 1) {
                    return o;
                } else if (o instanceof Map) {
                    map = (Map<String, Object>) o;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void addCommaSeparatedValue(String key, String value) {
        if (containsNested(key)) {
            addNested(key, getNested(key) + "," + value);
        } else {
            addNested(key, value);
        }
    }

    public void addListItem(String key, String value) {
        addNested(key, valuesList(key, value));
    }

    private List<String> valuesList(String key, String value) {
        if (containsNested(key)) {
            List<String> values = new ArrayList<>((List<String>) getNested(key));
            values.add(value);
            return values;
        }
        return Collections.singletonList(value);
    }
}
