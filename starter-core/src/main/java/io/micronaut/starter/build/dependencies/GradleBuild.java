package io.micronaut.starter.build.dependencies;

import java.util.Collections;
import java.util.List;

public class GradleBuild {
    private final GradleDsl dsl;
    private final List<Dependency> dependencies;
    private final List<MavenCoordinate> annotationProcessors;

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList(), Collections.emptyList());
    }

    public GradleBuild(GradleDsl gradleDsl,
                       List<Dependency> dependencies,
                       List<MavenCoordinate> annotationProcessors) {
        this.dsl = gradleDsl;
        this.dependencies = dependencies;
        this.annotationProcessors = annotationProcessors;
    }

    public GradleDsl getDsl() {
        return dsl;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public List<MavenCoordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }
}
