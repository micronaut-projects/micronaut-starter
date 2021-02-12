package io.micronaut.starter.build.dependencies;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class GradleBuildToolDependencyResolver extends DependencyResolver {
    public GradleBuildToolDependencyResolver(MavenCoordinateResolver mavenCoordinateResolver,
                                             AdapterBuilder adapterBuilder) {
        super(mavenCoordinateResolver, adapterBuilder, BuildTool.GRADLE, new GradleDependencyComparator());
    }

    public GradleBuild gradleBuild(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getBuildTool();

        List<Dependency> dependencies = resolve(generatorContext.getDependencies())
                .stream()
                .filter(dependency -> !dependency.getScope().isPresent() || (dependency.getScope().isPresent() && !dependency.getScope().get().equals(GradleConfiguration.ANNOTATION_PROCESSOR.toString())))
                .collect(Collectors.toList());
        List<MavenCoordinate> annotationProcessors = annotationProcessors(generatorContext.getDependencies());
        return new GradleBuild(buildTool == BuildTool.GRADLE_KOTLIN ? GradleDsl.KOTLIN : GradleDsl.GROOVY, dependencies, annotationProcessors);
    }
}
