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
package io.micronaut.starter.build.maven;

import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.dependencies.Dependency;

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
