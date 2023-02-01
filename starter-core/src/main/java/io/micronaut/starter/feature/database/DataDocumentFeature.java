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

import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;

/**
 * Marker features for data document features such as {@link DataMongo} and {@link DataAzureCosmosFeature}.
 * @author Sergio del Amo
 * @since 3.9.x
 */
public interface DataDocumentFeature extends DataFeature {
    String MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT = "micronaut-data-document-processor";
    Dependency DEPENDENCY_MICRONAUT_DATA_DOCUMENT_PROCESSOR = MicronautDependencyUtils.dataDependency()
            .artifactId(MICRONAUT_DATA_DOCUMENT_PROCESSOR_ARTIFACT)
            .versionProperty(MICRONAUT_DATA_VERSION)
            .annotationProcessor(true)
            .build();
}
