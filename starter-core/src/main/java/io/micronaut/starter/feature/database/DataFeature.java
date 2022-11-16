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
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.OneOfFeature;

import java.util.Collections;
import java.util.Map;

public interface DataFeature extends OneOfFeature {

    Dependency.Builder DEPENDENCY_MICRONAUT_DATA_PROCESSOR = MicronautDependencyUtils.dataDependency()
            .artifactId("micronaut-data-processor")
            .versionProperty("micronaut.data.version")
            .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
            .annotationProcessor(true);

    @Override
    default Class<?> getFeatureClass() {
        return DataFeature.class;
    }

    default Map<String, Object> getDatasourceConfig(DatabaseDriverFeature driverFeature) {
//        Map<String, Object> conf = new LinkedHashMap<>();
//        conf.put("datasources.default.schema-generate", "CREATE_DROP");
//        conf.put("datasources.default.dialect", driverFeature.getDataDialect());
//        return conf;
        return Collections.singletonMap("datasources.default.dialect", driverFeature.getDataDialect());
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
