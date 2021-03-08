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
package io.micronaut.starter.build;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildProperties {

    private final Map<String, Property> propertyMap = new LinkedHashMap<>();

    public void putAll(Map<String, String> map) {
        for (String k : map.keySet()) {
            put(k, map.get(k));
        }
    }

    public void put(String key, String value) {
        propertyMap.put(key, new Property() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }
        });
    }

    public void addComment(String comment) {
        propertyMap.put(comment, (Comment) () -> comment);
    }

    public List<Property> getProperties() {
        return new ArrayList<>(propertyMap.values());
    }
}
