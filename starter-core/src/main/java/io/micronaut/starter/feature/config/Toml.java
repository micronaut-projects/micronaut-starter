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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.TomlTemplate;
import jakarta.inject.Singleton;

import java.util.function.Function;

@Singleton
public class Toml implements ConfigurationFeature {

    public static final String NAME = "toml";
    private static final String EXTENSION = "toml";

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        // as a config feature, we're processed last, after the build tools. We need to add the dependency to
        // micronaut-toml before that.
        featureContext.addFeature(new Feature() {
            @Override
            public String getName() {
                return "toml-build";
            }

            @Override
            public boolean supports(ApplicationType applicationType) {
                return true;
            }

            @Override
            public void apply(GeneratorContext generatorContext) {
                generatorContext.addDependency(Dependency.builder()
                        .groupId("io.micronaut.toml")
                        .artifactId("micronaut-toml")
                        .compile());
            }
        });
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "TOML configuration";
    }

    @Override
    public String getDescription() {
        return "Creates a TOML configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return cfg -> new TomlTemplate(cfg.getFullPath(EXTENSION), cfg);
    }
}
