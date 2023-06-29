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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.micrometer.Prometheus;

import jakarta.inject.Singleton;

/**
 * Adds Dekorate Prometheus support that generates ServiceMonitor resource.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Singleton
public class DekoratePrometheus extends AbstractDekorateServiceFeature {

    private final Prometheus prometheus;

    public DekoratePrometheus(DekorateKubernetes dekorateKubernetes, Prometheus prometheus) {
        super(dekorateKubernetes);
        this.prometheus = prometheus;
    }

    @NonNull
    @Override
    public String getName() {
        return "dekorate-prometheus";
    }

    @Override
    public String getTitle() {
        return "Dekorate Prometheus Support";
    }

    @Override
    public String getDescription() {
        return "Extends Decorate's generated Kubernetes deployment manifests with Prometheus ServiceMonitor resource " +
                "using Dekorate Prometheus Support.";
    }

    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(Prometheus.class)) {
            featureContext.addFeature(prometheus);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Dependency.Builder prometheus = Dependency.builder()
                .groupId("io.dekorate")
                .artifactId("prometheus-annotations")
                .template();

        generatorContext.addDependency(prometheus.versionProperty("dekorate.version").annotationProcessor());
        generatorContext.addDependency(prometheus.compile());
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/dekorateio/dekorate#prometheus-annotations";
    }
}
