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
package io.micronaut.starter.feature.discovery;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.k8s.KubernetesClient;
import jakarta.inject.Singleton;

@Singleton
public class DiscoveryKubernetes implements DiscoveryFeature {
    private static final Dependency DEPENDENCY_MICRONAUT_DISCOVERY_K8S = Dependency.builder()
            .groupId(KubernetesClient.MICRONAUT_KUBERNETES_GROUP_ID)
            .artifactId("micronaut-kubernetes-discovery-client")
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "discovery-kubernetes";
    }

    @Override
    public String getTitle() {
        return "Kubernetes Service Discovery";
    }

    @Override
    public String getDescription() {
        return "Adds support for Service Discovery with Kubernetes";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getBootstrapConfiguration().put("kubernetes.client.discovery.mode", "endpoint");
        generatorContext.getBootstrapConfiguration().put("kubernetes.client.discovery.mode-configuration.endpoint.watch.enabled", true);
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DISCOVERY_K8S);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#service-discovery";
    }
}
