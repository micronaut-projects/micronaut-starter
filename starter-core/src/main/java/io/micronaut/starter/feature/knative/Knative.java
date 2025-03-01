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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.jib.Jib;
import io.micronaut.starter.feature.knative.template.knativeYaml;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

/**
 * Adds Knative configuration to an application.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Requires(property = "micronaut.starter.feature.knative.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && (mnOptions.applicationType() == ApplicationType.DEFAULT || mnOptions.applicationType() == ApplicationType.GRPC);
    }

    @Nullable
    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://knative.dev/";
    }
}
