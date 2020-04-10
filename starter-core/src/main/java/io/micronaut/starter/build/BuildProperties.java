package io.micronaut.starter.build;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildProperties {

    private final Map<String, Property> propertyMap = new LinkedHashMap<>();

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
