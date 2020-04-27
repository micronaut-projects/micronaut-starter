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
package io.micronaut.starter.feature.postgres;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class PostgresReactive implements Feature {

    @Override
    public String getName() {
        return "postgres-reactive";
    }

    @Override
    public String getTitle() {
        return "Postgres Reactive Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Reactive Postgres driver in the application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("postgres.reactive.client.port", 5432);
        generatorContext.getConfiguration().put("postgres.reactive.client.host", "localhost");
        generatorContext.getConfiguration().put("postgres.reactive.client.database", generatorContext.getProject().getName());
        generatorContext.getConfiguration().put("postgres.reactive.client.database.user", "user");
        generatorContext.getConfiguration().put("postgres.reactive.client.database.password", "password");
        generatorContext.getConfiguration().put("postgres.reactive.client.database.maxSize", 5);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public Category getCategory() {
        return Category.DATABASE;
    }
}
