package io.micronaut.starter.build.dependencies;

import io.micronaut.starter.build.Property;

import java.util.Collections;
import java.util.List;

public class MavenBuild {

    private final List<MavenCoordinate> annotationProcessors;

    private final List<Dependency> dependencies;

    private final List<Property> properties;

    public MavenBuild() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public MavenBuild(List<MavenCoordinate> annotationProcessors,
                      List<Dependency> dependencies,
                      List<Property> properties) {
        this.annotationProcessors = annotationProcessors;
        this.dependencies = dependencies;
        this.properties = properties;
    }

    public List<MavenCoordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public List<Property> getProperties() {
        return properties;
    }
}
