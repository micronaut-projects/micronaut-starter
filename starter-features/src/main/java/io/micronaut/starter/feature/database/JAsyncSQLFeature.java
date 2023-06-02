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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class JAsyncSQLFeature implements Feature {

    @Override
    public String getName() {
        return "jasync-sql";
    }

    @Override
    public String getTitle() {
        return "jasync-sql Asynchronous Database Drivers";
    }

    @Override
    public String getDescription() {
        return "Adds support for JAsync asynchronous PostgreSQL and MySQL database drivers";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> configuration = generatorContext.getConfiguration();
        configuration.put("jasync.client.port", 5432);
        configuration.put("jasync.client.host", "the-host");
        configuration.put("jasync.client.database", "the-db");
        configuration.put("jasync.client.username", "test");
        configuration.put("jasync.client.password", "test");
        configuration.put("jasync.client.maxActiveConnections", 5);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.sql")
                .artifactId("micronaut-jasync-sql")
                .compile());
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
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jasync";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/jasync-sql/jasync-sql/wiki";
    }
}
