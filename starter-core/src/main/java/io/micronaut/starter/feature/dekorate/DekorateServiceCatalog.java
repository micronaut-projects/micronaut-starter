/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.dekorate;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;

import jakarta.inject.Singleton;

/**
 * Adds Dekorate Service Catalog support.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Singleton
public class DekorateServiceCatalog extends AbstractDekorateServiceFeature {

    public DekorateServiceCatalog(DekorateKubernetes dekorateKubernetes) {
        super(dekorateKubernetes);
    }

    @NonNull
    @Override
    public String getName() {
        return "dekorate-servicecatalog";
    }

    @Override
    public String getTitle() {
        return "Dekorate Service Catalog Support";
    }

    @Override
    public String getDescription() {
        return "Extends Decorate's generated Kubernetes deployment manifests with Service Catalog resources " +
                "using Dekorate Service Catalog Support";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/dekorateio/dekorate#service-catalog";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder servicecatalog = Dependency.builder()
                .groupId("io.dekorate")
                .lookupArtifactId("servicecatalog-annotations")
                .template();

        generatorContext.addDependency(servicecatalog.annotationProcessor());
        generatorContext.addDependency(servicecatalog.compile());
    }
}
