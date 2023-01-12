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
package io.micronaut.starter.feature.config;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.YamlTemplate;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.function.Function;

@Singleton
public class Yaml implements ConfigurationFeature, DefaultFeature {

    public static final String EXTENSION = "yml";

    @Override
    @NonNull
    public String getName() {
        return "yaml";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(f -> f instanceof ConfigurationFeature);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        // As a config feature, we're processed last, after the build tools.
        // Add the `org.yaml:snakeyaml` dependency before that.
        featureContext.addFeature(new Feature() {
            @Override
            @NonNull
            public String getName() {
                return "yaml-build";
            }

            @Override
            public boolean supports(ApplicationType applicationType) {
                return true;
            }

            @Override
            public void apply(GeneratorContext generatorContext) {
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.yaml")
                        .artifactId("snakeyaml")
                        .runtime());
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.yaml")
                        .artifactId("snakeyaml")
                        .testRuntime());
            }
        });
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return (config) -> new YamlTemplate(config.getFullPath(EXTENSION), config);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
