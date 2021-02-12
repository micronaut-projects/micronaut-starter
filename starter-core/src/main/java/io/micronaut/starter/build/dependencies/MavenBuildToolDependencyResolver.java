package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MavenBuildToolDependencyResolver extends DependencyResolver {

    public static final String PROPERTY_PREFIX = "${";
    public static final String PROPERTY_SUFFIX = ".version}";
    public static final String CLOSE_BRACKET = "}";
    private final MicronautVersionsPropertiesResolver propertiesResolver;

    public MavenBuildToolDependencyResolver(MavenCoordinateResolver mavenCoordinateResolver,
                                            AdapterBuilder adapterBuilder,
                                            MicronautVersionsPropertiesResolver propertiesResolver) {
        super(mavenCoordinateResolver, adapterBuilder, BuildTool.MAVEN, new MavenDependencyComparator());
        this.propertiesResolver = propertiesResolver;
    }

    @NonNull
    public Map<String, String> buildProperties(@NonNull List<MavenCoordinate> coordinates) {
        Map<String, String> result = new HashMap<>();
        for (MavenCoordinate coordinate : coordinates) {
            if (coordinate.getVersion() != null &&
                    coordinate.getVersion().startsWith(PROPERTY_PREFIX) &&
                    coordinate.getVersion().endsWith(PROPERTY_SUFFIX)) {
                String k = coordinate.getVersion().substring(coordinate.getVersion().indexOf(PROPERTY_PREFIX) + PROPERTY_PREFIX.length(),
                        coordinate.getVersion().indexOf(CLOSE_BRACKET));
                propertiesResolver.resolve(k).ifPresent(val -> result.put(k, val));
            }
        }
        return result;
    }

    @NonNull
    public MavenBuild mavenBuild(GeneratorContext generatorContext) {
        List<Dependency> dependencies = resolve(generatorContext.getDependencies());
        List<MavenCoordinate> annotationProcessors = annotationProcessors(generatorContext.getDependencies());
        generatorContext.getBuildProperties().putAll(buildProperties(annotationProcessors));
        return new MavenBuild(annotationProcessors, dependencies, generatorContext.getBuildProperties().getProperties());
    }
}
