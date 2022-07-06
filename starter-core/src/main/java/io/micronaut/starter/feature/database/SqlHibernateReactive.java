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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class SqlHibernateReactive extends TestContainersFeature implements HibernateReactiveFeature {

    public static final String NAME = "hibernate-reactive";

    private static final Dependency.Builder DEPENDENCY_MICRONAUT_SQL_HIBERNATE_REACTIVE = MicronautDependencyUtils.sqlDependency()
            .artifactId("micronaut-hibernate-reactive")
                .compile();

    public SqlHibernateReactive(TestContainers testContainers) {
        super(testContainers);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut SQL Hibernate Reactive";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Micronaut SQL Hibernate Reactive";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_SQL_HIBERNATE_REACTIVE);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate-reactive";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
