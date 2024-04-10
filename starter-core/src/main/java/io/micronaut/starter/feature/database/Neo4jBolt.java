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
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class Neo4jBolt implements Feature {

    public static final String NAME = "neo4j-bolt";

    private static final Dependency DEPENDENCY_NEO4J = MicronautDependencyUtils.neo4j()
            .artifactId("micronaut-neo4j-bolt")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Neo4j Bolt Driver";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for the Neo4j Bolt Driver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("neo4j.uri", "bolt://${NEO4J_HOST:localhost}");
        generatorContext.addDependency(DEPENDENCY_NEO4J);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-neo4j/latest/guide/index.html";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://neo4j.com/docs/java-manual/current/";
    }
}
