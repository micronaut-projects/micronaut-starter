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
package io.micronaut.starter.feature.knative;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.jib.Jib;
import io.micronaut.starter.feature.knative.template.knativeYaml;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

/**
 * Adds Knative configuration to an application.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Singleton
public class Knative implements Feature {

    private final Jib jib;
    private final Management management;

    public Knative(Jib jib, Management management) {
        this.jib = jib;
        this.management = management;
    }

    @NonNull
    @Override
    public String getName() {
        return "knative";
    }

    @Override
    public String getTitle() {
        return "Knative Support";
    }

    @Override
    public String getDescription() {
        return "Generates a Knative deployment descriptor for deployment to Kubernetes";
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Management.class)) {
            featureContext.addFeature(management);
        }
        if (!featureContext.isPresent(Jib.class)) {
            featureContext.addFeature(jib);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("knativeYaml", new RockerTemplate("knativeYaml.yml", knativeYaml.template(generatorContext.getProject())));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.GRPC;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://knative.dev/";
    }
}
