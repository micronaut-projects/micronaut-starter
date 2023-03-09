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
package io.micronaut.starter.feature.function.azure;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.azure.template.azureFunctionMavenPlugin;
import io.micronaut.starter.feature.function.azure.template.azurefunctions;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class AzureBuildPluginFeature implements AzureCloudFeature, FunctionFeature {
    private static final String NAME = "azure-build-plugin";

    private final CoordinateResolver coordinateResolver;
    private final AzureReadmeFeature azureReadmeFeature;

    public AzureBuildPluginFeature(CoordinateResolver coordinateResolver,
                                   AzureReadmeFeature azureReadmeFeature) {
        this.coordinateResolver = coordinateResolver;
        this.azureReadmeFeature = azureReadmeFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(AzureReadmeFeature.class, azureReadmeFeature);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getBuildTool();
        loadTemplates(generatorContext);
        if (buildTool.isGradle()) {
            generatorContext.addHelpLink("Azure Functions Plugin for Gradle", "https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions");
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.microsoft.azure.azurefunctions")
                    .lookupArtifactId("azure-functions-gradle-plugin")
                    .extension(new RockerWritable(azurefunctions.template(generatorContext.getProject(), generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY))))
                    .build());
        } else if (buildTool == BuildTool.MAVEN) {
            Project project = generatorContext.getProject();

            String mavenPluginArtifactId = "azure-functions-maven-plugin";
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .artifactId(mavenPluginArtifactId)
                    .extension(new RockerWritable(azureFunctionMavenPlugin.template()))
                    .build());
            BuildProperties props = generatorContext.getBuildProperties();
            coordinateResolver.resolve(mavenPluginArtifactId)
                    .ifPresent(coordinate -> props.put("azure.functions.maven.plugin.version", coordinate.getVersion()));
            props.put("functionAppName", project.getName());
            props.put("functionResourceGroup", "java-functions-group");
            props.put("functionAppRegion", "westus");
            props.put("functionRuntimeOs", "windows");
            javaVersionValue(generatorContext).ifPresent(value -> props.put("functionRuntimeJavaVersion", value));
            props.put("stagingDirectory", "${project.build.directory}/azure-functions/${functionAppName}");
        }
    }

    @NonNull
    private Optional<String> javaVersionValue(GeneratorContext generatorContext) {
        switch (generatorContext.getJdkVersion()) {
            case JDK_8:
                return Optional.of("8");
            case JDK_11:
                return Optional.of("11");
            default:
                return Optional.empty();
        }
    }

    private void loadTemplates(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("host.json", new URLTemplate("host.json", classLoader.getResource("functions/azure/host.json")));
        generatorContext.addTemplate("local.settings.json", new URLTemplate("local.settings.json", classLoader.getResource("functions/azure/local.settings.json")));
    }
}
