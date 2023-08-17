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
package io.micronaut.starter.feature.dekorate;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;

import jakarta.inject.Singleton;

/**
 * Adds Dekorate Service Halkyon support.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Singleton
public class DekorateHalkyon extends AbstractDekorateServiceFeature {

    public DekorateHalkyon(DekorateKubernetes dekorateKubernetes) {
        super(dekorateKubernetes);
    }

    @NonNull
    @Override
    public String getName() {
        return "dekorate-halkyon";
    }

    @Override
    public String getTitle() {
        return "Dekorate Halkyon Support";
    }

    @Override
    public String getDescription() {
        return """
                Extends Decorate's generated Kubernetes deployment manifests with Halkyon resources \
                using Dekorate Halkyon Support\
                """;
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/dekorateio/dekorate#halkyon-crd";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder halkyon = Dependency.builder()
                .groupId("io.dekorate")
                .artifactId("halkyon-annotations")
                .template();

        generatorContext.addDependency(halkyon.versionProperty("dekorate.version").annotationProcessor());
        generatorContext.addDependency(halkyon.compile());
    }
}
