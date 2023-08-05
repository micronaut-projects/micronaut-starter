/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.k8s;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.reactor.Reactor;
import jakarta.inject.Singleton;

/**
 * Adds micronaut-kubernetes-reactor-client.
 *
 * @author Pavol Gressa
 * @since 3.1
 */
@Singleton
public class KubernetesReactorClient implements Feature {

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "kubernetes-reactor-client";
    }

    @Override
    public String getTitle() {
        return "Kubernetes Reactor Client";
    }

    @Override
    public String getDescription() {
        return "Adds Official Kubernetes Java Client with Reactor interface";
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-client";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/kubernetes-client/java/wiki";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(KubernetesClient.class)) {
            featureContext.exclude(KubernetesClient.class::isInstance);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!generatorContext.isFeaturePresent(Reactor.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.projectreactor")
                    .artifactId("reactor-core")
                    .compile());
        }
        generatorContext.addDependency(Dependency.builder()
                .groupId(KubernetesClient.MICRONAUT_KUBERNETES_GROUP_ID)
                .artifactId("micronaut-kubernetes-client-reactor")
                .compile());
    }
}
