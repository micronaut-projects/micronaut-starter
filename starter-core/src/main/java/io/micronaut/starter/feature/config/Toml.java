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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.ConfigurationFeature;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.starter.template.TomlTemplate;
import jakarta.inject.Singleton;

import java.util.function.Function;

@Requires(property = "micronaut.starter.feature.toml.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    public Function<Configuration, Template> createTemplate() {
        return cfg -> new TomlTemplate(cfg.getFullPath(EXTENSION), cfg);
    }
}
