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
import jakarta.inject.Singleton;

@Singleton
public class KubernetesInformer implements Feature {

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "kubernetes-informer";
    }

    @Override
    public String getTitle() {
        return "Kubernetes Informer Support";
    }

    @Override
    public String getDescription() {
        return "Adds Micronaut Kubernetes Informer support";
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-informer";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/kubernetes-client/java/wiki";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(KubernetesClient.MICRONAUT_KUBERNETES_GROUP_ID)
                .artifactId("micronaut-kubernetes-informer")
                .compile());
    }
}
