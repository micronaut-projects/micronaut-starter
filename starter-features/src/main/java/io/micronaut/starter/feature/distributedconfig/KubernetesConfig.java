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
package io.micronaut.starter.feature.distributedconfig;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.k8s.Kubernetes;

import io.micronaut.starter.feature.k8s.KubernetesClient;
import jakarta.inject.Singleton;

/**
 * Adds support for Kubernetes config maps configuration.
 *
 * @author alvaro
 * @since 2.0.0
 */
@Singleton
public class KubernetesConfig implements DistributedConfigFeature {

    private final Kubernetes kubernetes;

    public KubernetesConfig(Kubernetes kubernetes) {
        this.kubernetes = kubernetes;
    }

    @Override
    public String getName() {
        return "config-kubernetes";
    }

    @Override
    public String getTitle() {
        return "Kubernetes Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Kubernetes ConfigMap";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Kubernetes.class)) {
            featureContext.addFeature(kubernetes);
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        populateBootstrapForDistributedConfiguration(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .groupId(KubernetesClient.MICRONAUT_KUBERNETES_GROUP_ID)
                .artifactId("micronaut-kubernetes-discovery-client")
                .compile());
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#config-client";
    }
}
