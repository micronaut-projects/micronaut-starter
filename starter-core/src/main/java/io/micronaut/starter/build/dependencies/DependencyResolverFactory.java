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

import io.micronaut.context.annotation.Factory;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Named;
import javax.inject.Singleton;

@Factory
public class DependencyResolverFactory {
    private final MavenCoordinateResolver mavenCoordinateResolver;
    private final AdapterBuilder adapterBuilder;

    public DependencyResolverFactory(MavenCoordinateResolver mavenCoordinateResolver,
                                     AdapterBuilder adapterBuilder) {
        this.mavenCoordinateResolver = mavenCoordinateResolver;
        this.adapterBuilder = adapterBuilder;
    }

    @Named("gradle")
    @Singleton
    public BuildToolDependencyResolver createGradle() {
        return new DependencyResolver(mavenCoordinateResolver, adapterBuilder, BuildTool.GRADLE, new GradleDependencyComparator());
    }

    @Named("maven")
    @Singleton
    public BuildToolDependencyResolver createMaven() {
        return new DependencyResolver(mavenCoordinateResolver, adapterBuilder, BuildTool.MAVEN, new MavenDependencyComparator());
    }
}
