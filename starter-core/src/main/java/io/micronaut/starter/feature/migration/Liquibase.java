/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.migration;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;

import javax.inject.Singleton;

@Singleton
public class Liquibase implements MigrationFeature {

    @Override
    public String getName() {
        return "liquibase";
    }

    @Override
    public String getTitle() {
        return "Liquibase Database Migration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Liquibase database migrations";
    }

    public String getThirdPartyDocumentation() {
        return "https://www.liquibase.org/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.liquibase")
                .artifactId("micronaut-liquibase")
                .compile());
    }
}
