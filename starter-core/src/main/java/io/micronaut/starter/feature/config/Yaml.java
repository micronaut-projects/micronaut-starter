/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.config;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.YamlTemplate;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

@Singleton
public class Yaml implements ConfigurationFeature, DefaultFeature {

    @Override
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
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("yamlConfig", new YamlTemplate("src/main/resources/application.yml", generatorContext.getConfiguration()));
        if (!generatorContext.getBootstrapConfig().isEmpty()) {
            generatorContext.addTemplate("yamlBootstrapConfig", new YamlTemplate("src/main/resources/bootstrap.yml", generatorContext.getBootstrapConfig()));
        }
        Map<String, EnvConfiguration> envConfigs = generatorContext.getEnvConfigurations();
        if (!envConfigs.isEmpty()) {
            for (Map.Entry<String, EnvConfiguration> envConfig: envConfigs.entrySet()) {
                String env = envConfig.getKey();
                Map<String, Object> configs = envConfig.getValue().getEnvConfiguration();
                String path = envConfig.getValue().getPath();
                generatorContext.addTemplate("yamlConfig" + StringUtils.capitalize(env),
                    new YamlTemplate(path + "application-" + env + ".yml", configs));
            }
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
