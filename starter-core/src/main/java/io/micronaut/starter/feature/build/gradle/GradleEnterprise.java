/*
 * Copyright 2017-2021 original authors
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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gradle.templates.gradleEnterprise;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class GradleEnterprise implements Feature, GradleEnterpriseConfiguration {

    public static final String GRADLE_ENTERPRISE_PLUGIN_ID = "com.gradle.enterprise";
    public static final String GRADLE_ENTERPRISE_ARTIFACT_ID = "gradle-enterprise-gradle-plugin";

    private final MavenGradleEnterprise mavenGradleEnterprise;

    public GradleEnterprise(MavenGradleEnterprise mavenGradleEnterprise) {
        this.mavenGradleEnterprise = mavenGradleEnterprise;
    }

    @Override
    @NonNull
    public String getName() {
        return "gradle-enterprise";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Gradle Enterprise";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Gradle Enterprise Gradle plugin which enables integration with Gradle Enterprise and scans.gradle.com";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .gradleFile(GradleFile.SETTINGS)
                    .id(GRADLE_ENTERPRISE_PLUGIN_ID)
                    .lookupArtifactId(GRADLE_ENTERPRISE_ARTIFACT_ID)
                    .settingsExtension(new RockerWritable(gradleEnterprise.template(this)))
                    .build());
        } else {
            mavenGradleEnterprise.applyMaven(generatorContext, this);
        }

    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gradle.com/enterprise/gradle-plugin/";
    }
}
