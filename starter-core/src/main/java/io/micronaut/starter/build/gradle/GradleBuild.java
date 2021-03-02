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
package io.micronaut.starter.build.gradle;

import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.maven.MavenCoordinate;

import java.util.Collections;
import java.util.List;

public class GradleBuild {
    private final GradleDsl dsl;
    private final List<Dependency> dependencies;
    private final List<MavenCoordinate> annotationProcessors;
    private final List<GradlePlugin> plugins;

    public GradleBuild() {
        this(GradleDsl.GROOVY, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public GradleBuild(GradleDsl gradleDsl,
                       List<Dependency> dependencies,
                       List<MavenCoordinate> annotationProcessors,
                       List<GradlePlugin> plugins) {
        this.dsl = gradleDsl;
        this.dependencies = dependencies;
        this.annotationProcessors = annotationProcessors;
        this.plugins = plugins;
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

    public List<GradlePlugin> getPlugins() {
        return plugins;
    }
}
