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
    public static final String NAME = "yaml";
    private static final String EXTENSION = "yml";
    private static final String YAML_GROUP_ID = "org.yaml";
    private static final String SNAKEYAML_ARTIFACT_ID = "snakeyaml";
    private static final Dependency DEPENDENCY_YAML = Dependency.builder()
            .groupId(YAML_GROUP_ID)
            .artifactId(SNAKEYAML_ARTIFACT_ID)
            .runtime()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Yaml";
    }

    @Override
    public String getDescription() {
        return "Adds support for using YAML for configuration";
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(ConfigurationFeature.class::isInstance);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        // as a config feature, we're processed last, after the build tools.
        //  We need to add the dependency before that.
        featureContext.addFeature(new Feature() {
            @Override
            public String getName() {
                return "yaml-build";
            }

            @Override
            public boolean supports(ApplicationType applicationType) {
                return true;
            }

            @Override
            public void apply(GeneratorContext generatorContext) {
                generatorContext.addDependency(DEPENDENCY_YAML);
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
