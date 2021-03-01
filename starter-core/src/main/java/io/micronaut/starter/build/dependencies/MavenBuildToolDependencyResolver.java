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
import java.util.Optional;

@Singleton
public class MavenBuildToolDependencyResolver extends DependencyResolver {

    private final PropertiesResolver propertiesResolver;

    public MavenBuildToolDependencyResolver(MavenCoordinateResolver mavenCoordinateResolver,
                                            AdapterBuilder adapterBuilder,
                                            PropertiesResolver propertiesResolver) {
        super(mavenCoordinateResolver, adapterBuilder, BuildTool.MAVEN, new MavenDependencyComparator());
        this.propertiesResolver = propertiesResolver;
    }

    @NonNull
    public Map<String, String> buildProperties(@NonNull List<MavenCoordinate> coordinates) {
        Map<String, String> result = new HashMap<>();
        for (MavenCoordinate coordinate : coordinates) {
            Optional<String> kOptional = propertiesResolver.getPropertyKey(coordinate.getVersion());
            if (kOptional.isPresent()) {
                String k = kOptional.get();
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
