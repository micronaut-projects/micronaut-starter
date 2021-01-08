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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;

import javax.inject.Singleton;

@Singleton
public class MongoSync extends MongoFeature {

    public MongoSync(TestContainers testContainers) {
        super(testContainers);
    }

    @Override
    public String getName() {
        return "mongo-sync";
    }

    @Override
    public String getTitle() {
        return "Mongo Synchronous Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Mongo Synchronous Driver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("mongodb.uri", "mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}");
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
        return "https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.mongodb.com";
    }
}
