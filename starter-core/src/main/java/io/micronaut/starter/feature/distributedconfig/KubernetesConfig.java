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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.features.config.MicronautDistributedConfigurationFeature;
import io.micronaut.starter.feature.k8s.Kubernetes;

import io.micronaut.starter.feature.k8s.KubernetesClient;
import jakarta.inject.Singleton;

/**
 * Adds support for Kubernetes config maps configuration.
 *
 * @author alvaro
 * @since 2.0.0
 */
@Requires(property = "micronaut.starter.feature.config.kubernetes.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class KubernetesConfig implements MicronautDistributedConfigurationFeature {

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
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions &&
                (mnOptions.applicationType() != ApplicationType.CLI && mnOptions.applicationType() != ApplicationType.FUNCTION);
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
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#config-client";
    }
}
