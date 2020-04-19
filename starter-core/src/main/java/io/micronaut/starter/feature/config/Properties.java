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

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.template.PropertiesTemplate;
import io.micronaut.starter.template.YamlTemplate;

import javax.inject.Singleton;

@Singleton
public class Properties implements ConfigurationFeature {

    @Override
    public String getName() {
        return "properties";
    }

    @Override
    public String getTitle() {
        return "Java Properties Configuration";
    }

    @Override
    public String getDescription() {
        return "Creates a properties configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("propertiesConfig", new PropertiesTemplate("src/main/resources/application.properties", commandContext.getConfiguration()));
        if (!commandContext.getBootstrapConfig().isEmpty()) {
            commandContext.addTemplate("propertiesBootstrapConfig", new YamlTemplate("src/main/resources/bootstrap.properties", commandContext.getBootstrapConfig()));
        }
    }
}
