/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesTemplate extends DefaultTemplate {

    private final Properties properties;

    public PropertiesTemplate(String path, Map<String, Object> config) {
        this(DEFAULT_MODULE, path, config);
    }

    public PropertiesTemplate(String module, String path, Map<String, Object> config) {
        super(module, path);
        this.properties = transform(new LinkedProperties(), "", config);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        properties.store(outputStream, null);
    }

    private Properties transform(Properties finalConfig, String prefix, Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            transform(finalConfig, prefix + entry.getKey(), entry.getValue());
        }
        return finalConfig;
    }

    private void transform(Properties finalConfig, String prefix, Object value) {
        if (value instanceof Map) {
            transform(finalConfig, prefix + ".", (Map<String, Object>) value);
        } else if (value instanceof List list) {
            for (int i = 0; i < list.size(); i++) {
                transform(finalConfig, prefix + "[" + i + "]", list.get(i));
            }
        } else {
            finalConfig.put(prefix, value.toString());
        }
    }

    public class LinkedProperties extends Properties {
        private final HashSet<Object> keys = new LinkedHashSet<>();

        public LinkedProperties() {
        }

        public Iterable<Object> orderedKeys() {
            return Collections.list(keys());
        }

        public Enumeration<Object> keys() {
            return Collections.enumeration(keys);
        }

        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }
    }
}
