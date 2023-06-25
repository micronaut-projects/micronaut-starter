/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.dev;

import io.micronaut.context.env.Environment;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.other.Management;
import jakarta.inject.Singleton;

/**
 * Micronaut Control Panel feature.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 4.0.0
 */
@Singleton
public class ControlPanel implements Feature {

    public static final String NAME = "control-panel";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Control Panel";
    }

    @Override
    public String getDescription() {
        return "The Micronaut Control Panel module provides a web UI that allows you to view and manage the state of " +
                "your Micronaut application, typically in a development environment";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-control-panel/latest/guide/index.html";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.controlPanelDependency().artifactId("micronaut-control-panel-ui"));
        ApplicationConfiguration devConfig = generatorContext.getConfiguration(Environment.DEVELOPMENT, ApplicationConfiguration.devConfig());

        if (generatorContext.isFeaturePresent(Management.class)) {
            generatorContext.addDependency(MicronautDependencyUtils.controlPanelDependency().artifactId("micronaut-control-panel-management"));
            devConfig.put("endpoints.all.enabled", true);
            devConfig.put("endpoints.all.sensitive", false);
            devConfig.put("endpoints.health.details-visible", "ANONYMOUS");
            devConfig.put("endpoints.loggers.write-sensitive", false);
        }
    }
}
