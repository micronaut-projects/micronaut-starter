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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.options.BuildTool;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_DATA;

public interface DataFeature extends OneOfFeature {

    String SCHEMA_GENERATE_KEY = "datasources.default.schema-generate";
    String MICRONAUT_DATA_VERSION = "micronaut.data.version";
    String MICRONAUT_DATA_PROCESSOR_ARTIFACT = "micronaut-data-processor";

    @Override
    default Class<?> getFeatureClass() {
        return DataFeature.class;
    }

    default Map<String, Object> getDatasourceConfig(GeneratorContext generatorContext, DatabaseDriverFeature driverFeature) {
        Map<String, Object> conf = new LinkedHashMap<>();
        if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
            conf.put("datasources.default.schema-generate", "CREATE_DROP");
        }
        conf.put("datasources.default.dialect", driverFeature.getDataDialect());
        return conf;
    }

    static Dependency dataProcessorDependency(BuildTool buildTool) {
        return dataProcessorDependency(buildTool, MICRONAUT_DATA_PROCESSOR_ARTIFACT, Priority.MICRONAUT_DATA_PROCESSOR.getOrder());
    }

    static Dependency dataProcessorDependency(BuildTool buildTool, String artifactId, int order) {
        return MicronautDependencyUtils.annotationProcessor(buildTool, GROUP_ID_MICRONAUT_DATA, artifactId, MICRONAUT_DATA_VERSION, true)
                .order(order)
                .build();
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.DATABASE;
    }
}
