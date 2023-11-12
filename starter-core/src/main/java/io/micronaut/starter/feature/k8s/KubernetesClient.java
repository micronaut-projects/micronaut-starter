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
package io.micronaut.starter.feature.k8s;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.discovery.DiscoveryCore;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

/**
 * Adds micronaut-kubernetes-client that integrates official K8S SDK.
 *
 * @author Pavol Gressa
 * @since 3.1
 */
@Singleton
public class KubernetesClient implements Feature {

    public static final String MICRONAUT_KUBERNETES_GROUP_ID = "io.micronaut.kubernetes";

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "kubernetes-client";
    }

    @Override
    public String getTitle() {
        return "Official Kubernetes Java Client";
    }

    @Override
    public String getDescription() {
        return "Adds official Kubernetes java client with Micronaut support";
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
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(MICRONAUT_KUBERNETES_GROUP_ID)
                .artifactId("micronaut-kubernetes-client")
                .compile());
        fixupDependencies(generatorContext);
    }

    static void fixupDependencies(GeneratorContext generatorContext) {
        if (!generatorContext.hasFeature(DiscoveryCore.class)
                && generatorContext.getBuildTool() == BuildTool.MAVEN
                && generatorContext.getLanguage() == Language.GROOVY
        ) {
            // Maven requires discovery core provided to work with http-validation under groovy
            generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                    .artifactId(DiscoveryCore.ARTIFACT_ID_MICRONAUT_DISCOVERY_CORE)
                    .compileOnly());
        }
    }
}
