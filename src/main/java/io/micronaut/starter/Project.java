package io.micronaut.starter;

import java.util.HashMap;
import java.util.Map;

public class Project {

    private final String packageName;
    private final String packagePath;
    private final String className;
    private final String naturalName;
    private final String propertyName;
    private final String name;


    public Project(String packageName, String packagePath, String className, String naturalName, String propertyName, String name) {
        this.packageName = packageName;
        this.packagePath = packagePath;
        this.className = className;
        this.naturalName = naturalName;
        this.propertyName = propertyName;
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String getClassName() {
        return className;
    }

    public String getNaturalName() {
        return naturalName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("packageName", packageName);
        properties.put("packagePath", packagePath);
        properties.put("className", className);
        properties.put("naturalName", naturalName);
        properties.put("propertyName", propertyName);
        properties.put("name", name);
        return properties;
    }
}
