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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;

import jakarta.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class DataR2dbc implements R2dbcFeature {
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
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-processor")
                .versionProperty("micronaut.data.version")
                .annotationProcessor());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-r2dbc")
                .compile());
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().putAll(getDatasourceConfig(dbFeature));
    }

    private Map<? extends String, ?> getDatasourceConfig(DatabaseDriverFeature dbFeature) {
        Map<String, Object> conf = new LinkedHashMap<>();
        conf.put("r2dbc.datasources.default.schema-generate", "CREATE_DROP");
        conf.put("r2dbc.datasources.default.dialect", dbFeature.getDataDialect());
        return conf;
    }

    @NonNull
    @Override
    public String getName() {
        return "data-r2dbc";
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data R2DBC";
    }

    @Override
    public String getDescription() {
        return "Micronaut Data support for Reactive Database Connectivity (R2DBC)";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
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

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
