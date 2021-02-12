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
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;
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
