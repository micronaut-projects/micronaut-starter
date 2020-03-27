package io.micronaut.starter;

import java.util.HashMap;
import java.util.Map;

public class Project {

    private final String packageName;
    private final String packagePath;
    private final String className;
    private final String naturalName;
    private final String propertyName;
    private final String appName;


    public Project(String packageName, String packagePath, String className, String naturalName, String propertyName, String appName) {
        this.packageName = packageName;
        this.packagePath = packagePath;
        this.className = className;
        this.naturalName = naturalName;
        this.propertyName = propertyName;
        this.appName = appName;
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

    public String getAppName() {
        return appName;
    }

    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("packageName", packageName);
        properties.put("packagePath", packagePath);
        properties.put("className", className);
        properties.put("naturalName", naturalName);
        properties.put("propertyName", propertyName);
        properties.put("appName", appName);
        return properties;
    }
}
