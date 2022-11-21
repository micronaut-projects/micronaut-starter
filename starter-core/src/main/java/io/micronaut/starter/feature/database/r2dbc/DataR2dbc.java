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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DataFeature;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.TransactionalNotSupported;
import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class DataR2dbc implements R2dbcFeature, DataFeature, TransactionalNotSupported {

    public static final String NAME = "data-r2dbc";

    private static final Dependency DEPENDENCY_MICRONAUT_DATA_R2DBC = MicronautDependencyUtils.dataDependency()
            .artifactId("micronaut-data-r2dbc")
            .compile()
            .build();

    private final Data data;
    private final R2dbc r2dbc;

    public DataR2dbc(Data data, R2dbc r2dbc) {
        this.data = data;
        this.r2dbc = r2dbc;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
        if (!featureContext.isPresent(R2dbc.class)) {
            featureContext.addFeature(r2dbc);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DATA_PROCESSOR);
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DATA_R2DBC);

        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().addNested(getDatasourceConfig(generatorContext, dbFeature));
    }

    @Override
    public Map<String, Object> getDatasourceConfig(GeneratorContext generatorContext, DatabaseDriverFeature driverFeature) {
        Map<String, Object> conf = new LinkedHashMap<>();
        if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
            conf.put("r2dbc.datasources.default.schema-generate", "CREATE_DROP");
        }
        conf.put("r2dbc.datasources.default.dialect", driverFeature.getDataDialect());
        return conf;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data R2DBC";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Micronaut Data support for Reactive Database Connectivity (R2DBC)";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#dbc";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://r2dbc.io";
    }
}
