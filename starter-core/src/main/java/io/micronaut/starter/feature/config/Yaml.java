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
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.YamlTemplate;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.function.Function;

@Singleton
public class Yaml implements ConfigurationFeature {
    public static final String NAME = "yaml";
    private static final String EXTENSION = "yml";
    private static final String YAML_GROUP_ID = "org.yaml";
    private static final String SNAKEYAML_ARTIFACT_ID = "snakeyaml";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        // As a config feature, we're processed last, after the build tools.
        // Add the `org.yaml:snakeyaml` dependency before that.

        if (isVisible()) {
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
                    addDependencies(generatorContext);
                }

                private void addDependencies(GeneratorContext generatorContext) {
                    Arrays.asList(
                            snakeYamlDependency().runtime(),
                            snakeYamlDependency().testRuntime()
                    ).forEach(generatorContext::addDependency);
                }
            });
        }
    }

    private static Dependency.Builder snakeYamlDependency() {
        return Dependency.builder()
                .groupId(YAML_GROUP_ID)
                .artifactId(SNAKEYAML_ARTIFACT_ID);
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
