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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleFile;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.maven.templates.customData;
import jakarta.inject.Singleton;

@Singleton
public class MicronautGradleEnterprise implements Feature, GradleEnterpriseConfiguration {
    public static final String NAME = "micronaut-gradle-enterprise";
    private static final String SERVER = "https://ge.micronaut.io";
    private static final String GE_CUSTOM_USER_DATA_GROOVY = "gradle-enterprise-custom-user-data.groovy";
    private static final String ARTIFACT_ID_MICRONAUT_GRADLE_PLUGINS = "micronaut-gradle-plugins";
    private static final String GRADLE_PLUGIN_ID_MICRONAUT_GRADLE_ENTERPRISE = "io.micronaut.build.internal.gradle-enterprise";

    private final MavenGradleEnterprise mavenGradleEnterprise;

    public MicronautGradleEnterprise(MavenGradleEnterprise mavenGradleEnterprise) {
        this.mavenGradleEnterprise = mavenGradleEnterprise;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Gradle Enterprise";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getServer() {
        return SERVER;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            applyGradle(generatorContext);
        }  else {
            mavenGradleEnterprise.applyMaven(generatorContext, this);
            mavenGradleEnterprise.addMavenTemplate(generatorContext, GE_CUSTOM_USER_DATA_GROOVY, customData.template());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    private void applyGradle(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .gradleFile(GradleFile.SETTINGS)
                .id(GRADLE_PLUGIN_ID_MICRONAUT_GRADLE_ENTERPRISE)
                .lookupArtifactId(ARTIFACT_ID_MICRONAUT_GRADLE_PLUGINS)
                .requiresSettingsPluginsManagement()
                .build());
    }
}
