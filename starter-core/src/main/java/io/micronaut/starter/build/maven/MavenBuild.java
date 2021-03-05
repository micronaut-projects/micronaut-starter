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

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.Property;

import java.util.Collections;
import java.util.List;

public class MavenBuild {

    private final MavenCombineAttribute annotationProcessorCombineAttribute;

    private final MavenCombineAttribute testAnnotationProcessorCombineAttribute;

    private final List<Coordinate> testAnnotationProcessors;

    private final List<Coordinate> annotationProcessors;

    private final List<MavenDependency> dependencies;

    private final List<Property> properties;

    public MavenBuild() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MavenCombineAttribute.APPEND, MavenCombineAttribute.APPEND);
    }

    public MavenBuild(@NonNull List<Coordinate> annotationProcessors,
                      @NonNull List<Coordinate> testAnnotationProcessors,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<Property> properties,
                      @NonNull MavenCombineAttribute annotationProcessorCombineAttribute,
                      @NonNull MavenCombineAttribute testAnnotationProcessorCombineAttribute) {
        this.annotationProcessors = annotationProcessors;
        this.testAnnotationProcessors = testAnnotationProcessors;
        this.dependencies = dependencies;
        this.properties = properties;
        this.annotationProcessorCombineAttribute = annotationProcessorCombineAttribute;
        this.testAnnotationProcessorCombineAttribute = testAnnotationProcessorCombineAttribute;
    }

    @NonNull
    public List<Coordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }

    @NonNull
    public List<Coordinate> getTestAnnotationProcessors() {
        return testAnnotationProcessors;
    }

    @NonNull
    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @NonNull
    public List<Property> getProperties() {
        return properties;
    }

    public MavenCombineAttribute getAnnotationProcessorCombineAttribute() {
        return annotationProcessorCombineAttribute;
    }

    @NonNull
    public MavenCombineAttribute getTestAnnotationProcessorCombineAttribute() {
        return testAnnotationProcessorCombineAttribute;
    }
}
