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
package io.micronaut.starter.feature.build;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleFile;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gradle.templates.gradleEnterprise;
import io.micronaut.starter.feature.build.maven.templates.extensions;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class GradleEnterprise implements Feature, GradleEnterpriseConfiguration {
    public static final String GRADLE_ENTERPRISE_PLUGIN_ID = "com.gradle.enterprise";
    public static final String GRADLE_ENTERPRISE_ARTIFACT_ID = "gradle-enterprise-gradle-plugin";
    private static final String SLASH = "/";
    private static final String MAVEN_FOLDER = ".mvn";
    private static final String EXTENSIONS_XML = "extensions.xml";
    private static final String GRADLE_ENTERPRISE_XML = "gradle-enterprise.xml";
    private static final String DOT = ".";
    private static final String ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION = "gradle-enterprise-maven-extension";
    private static final String ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION = "common-custom-user-data-maven-extension";

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
            generatorContext.addBuildPlugin(gradlePlugin(this));
        } else {
            applyMaven(generatorContext, this);
        }
    }

    protected GradlePlugin gradlePlugin(GradleEnterpriseConfiguration configuration) {
        return GradlePlugin.builder()
                .gradleFile(GradleFile.SETTINGS)
                .id(GRADLE_ENTERPRISE_PLUGIN_ID)
                .lookupArtifactId(GRADLE_ENTERPRISE_ARTIFACT_ID)
                .settingsExtension(new RockerWritable(gradleEnterprise.template(configuration)))
                .build();
    }

    protected void applyMaven(GeneratorContext generatorContext, GradleEnterpriseConfiguration server) {
        addMavenTemplate(generatorContext, EXTENSIONS_XML, extensionsRockerModel(generatorContext));
        addMavenTemplate(generatorContext, GRADLE_ENTERPRISE_XML, io.micronaut.starter.feature.build.maven.templates.gradleEnterprise.template(server));
    }

    protected void addMavenTemplate(GeneratorContext generatorContext, String name, RockerModel rockerModel) {
        String templateName = name.contains(DOT) ? name.substring(0, name.indexOf(DOT)) : name;
        String path = String.join(SLASH, MAVEN_FOLDER, name);
        generatorContext.addTemplate(templateName, new RockerTemplate(path, rockerModel));
    }

    private static RockerModel extensionsRockerModel(GeneratorContext generatorContext) {
        return extensions.template(
                generatorContext.resolveCoordinate(ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION).getVersion(),
                generatorContext.resolveCoordinate(ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION).getVersion());
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
