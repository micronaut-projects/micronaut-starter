/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.dependencies.Coordinate;

import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.feature.build.maven.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import io.micronaut.starter.template.Writable;
import io.micronaut.starter.template.WritableUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MavenBuild {
    private static final Logger LOG = LoggerFactory.getLogger(MavenBuild.class);

    private final MavenCombineAttribute annotationProcessorCombineAttribute;

    private final MavenCombineAttribute testAnnotationProcessorCombineAttribute;

    private final List<DependencyCoordinate> testAnnotationProcessors;

    private final List<DependencyCoordinate> annotationProcessors;

    private final List<MavenDependency> dependencies;

    private final List<MavenPlugin> plugins;

    private final List<Property> properties;

    private final Collection<Profile> profiles;

    private final List<MavenRepository> repositories;

    private final List<DependencyCoordinate> aotDependencies;
    private final List<MavenCoordinate> testResourcesDependencies;

    @NonNull
    private final String artifactId;

    public MavenBuild(String artifactId) {
        this(artifactId,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                MavenCombineAttribute.APPEND,
                MavenCombineAttribute.APPEND,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }

    public MavenBuild(@NonNull String artifactId,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<MavenPlugin> plugins,
                      @NonNull List<MavenRepository> repositories) {
        this(artifactId,
                Collections.emptyList(),
                Collections.emptyList(),
                dependencies,
                Collections.emptyList(),
                plugins,
                repositories,
                MavenCombineAttribute.APPEND,
                MavenCombineAttribute.APPEND,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }

    public MavenBuild(@NonNull String artifactId,
                      @NonNull List<DependencyCoordinate> annotationProcessors,
                      @NonNull List<DependencyCoordinate> testAnnotationProcessors,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<Property> properties,
                      @NonNull List<MavenPlugin> plugins,
                      @NonNull List<MavenRepository> repositories,
                      @NonNull MavenCombineAttribute annotationProcessorCombineAttribute,
                      @NonNull MavenCombineAttribute testAnnotationProcessorCombineAttribute,
                      @NonNull Collection<Profile> profiles,
                      @NonNull List<DependencyCoordinate> aotDependencies,
                      @NonNull List<MavenCoordinate> testResourcesDependencies) {
        this.artifactId = artifactId;
        this.annotationProcessors = annotationProcessors;
        this.testAnnotationProcessors = testAnnotationProcessors;
        this.dependencies = dependencies;
        this.properties = properties;
        this.plugins = plugins;
        this.repositories = repositories;
        this.annotationProcessorCombineAttribute = annotationProcessorCombineAttribute;
        this.testAnnotationProcessorCombineAttribute = testAnnotationProcessorCombineAttribute;
        this.profiles = profiles;
        this.aotDependencies = aotDependencies;
        this.testResourcesDependencies = testResourcesDependencies;
    }

    @NonNull
    public String getArtifactId() {
        return artifactId;
    }

    @NonNull
    public String renderRepositories(int indentationSpaces) {
        return WritableUtils.renderWritableList(this.repositories.stream()
                .map(Writable.class::cast)
                .collect(Collectors.toList()), indentationSpaces);
    }

    @NonNull
    public String renderPlugins(int indentationSpaces) {
        List<Writable> writableList = plugins.stream()
                .map(MavenPlugin::getExtension)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return WritableUtils.renderWritableList(writableList, indentationSpaces);
    }

    @NonNull
    public List<DependencyCoordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }

    @NonNull
    public List<DependencyCoordinate> getTestAnnotationProcessors() {
        return testAnnotationProcessors;
    }

    @NonNull
    public Collection<Profile> getProfiles() {
        return profiles;
    }

    @NonNull
    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @NonNull
    public List<MavenDependency> getDependencies(boolean pom) {
        return dependencies
                .stream()
                .filter(it -> it.isPom() == pom)
                .collect(Collectors.toList());
    }

    public boolean hasPomDependency() {
        return dependencies.stream().anyMatch(Coordinate::isPom);
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

    public List<DependencyCoordinate> getAotDependencies() {
        return aotDependencies;
    }

    public List<MavenCoordinate> getTestResourcesDependencies() {
        return testResourcesDependencies;
    }

}
