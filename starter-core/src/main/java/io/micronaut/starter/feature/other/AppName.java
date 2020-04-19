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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.Options;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.externalconfig.ExternalConfigFeature;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class AppName implements DefaultFeature {

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Options options, List<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public String getName() {
        return "app-name";
    }

    @Override
    public String getTitle() {
        return "Application Name Support";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOWEST.getOrder();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(CommandContext commandContext) {
        Map<String, Object> appNameConfig;
        if (commandContext.isFeaturePresent(ExternalConfigFeature.class)) {
            appNameConfig = commandContext.getBootstrapConfig();
        } else {
            appNameConfig = commandContext.getConfiguration();
        }
        appNameConfig.put("micronaut.application.name", commandContext.getProject().getName());
    }
}
