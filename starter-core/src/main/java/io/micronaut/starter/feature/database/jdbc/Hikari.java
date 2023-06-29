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
package io.micronaut.starter.feature.database.jdbc;

import io.micronaut.context.annotation.Primary;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import jakarta.inject.Singleton;

@Singleton
@Primary
public class Hikari extends JdbcFeature {

    public static final String NAME = "jdbc-hikari";
    public static final String MICRONAUT_JDBC_HIKARI_ARTIFACT = "micronaut-jdbc-hikari";

    public Hikari(DatabaseDriverFeature dbFeature) {
        super(dbFeature);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Hikari JDBC Connection Pool";
    }

    @Override
    public String getDescription() {
        return "Configures SQL DataSources using Hikari Connection Pool";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(MicronautDependencyUtils.sqlDependency()
                .artifactId(MICRONAUT_JDBC_HIKARI_ARTIFACT)
                .compile());
    }
}
