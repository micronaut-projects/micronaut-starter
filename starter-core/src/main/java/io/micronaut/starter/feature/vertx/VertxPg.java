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
package io.micronaut.starter.feature.vertx;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ConfiguredFeature;
import io.micronaut.starter.feature.FeatureConfiguration;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class VertxPg extends ConfiguredFeature {

    public VertxPg(@Named("vertxpgclient") FeatureConfiguration featureConfiguration) {
        super(featureConfiguration);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("vertx.pg.client.port", 5432);
        generatorContext.getConfiguration().put("vertx.pg.client.host", "the-host");
        generatorContext.getConfiguration().put("vertx.pg.client.database", "the-db");
        generatorContext.getConfiguration().put("vertx.pg.client.database.user", "user");
        generatorContext.getConfiguration().put("vertx.pg.client.database.password", "password");
        generatorContext.getConfiguration().put("vertx.pg.client.database.maxSize", 5);
    }

}
