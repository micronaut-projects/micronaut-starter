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
package io.micronaut.starter.feature.function.azure;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.azure.template.azureFunctionMavenPlugin;
import io.micronaut.starter.feature.function.azure.template.azurefunctions;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionGroovyJunit;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionJavaJunit;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionKoTest;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionKotlinJunit;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionSpock;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerGroovy;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerJava;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerKotlin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.URLTemplate;

import java.util.Optional;

/**
 * Function impl for Azure.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractAzureFunction extends AbstractFunctionFeature implements AzureCloudFeature {

    public static final String NAME = "azure-function";

    private final CoordinateResolver coordinateResolver;

    public AbstractAzureFunction(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Azure Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to deploy to Microsoft Azure";
    }

    private void loadTemplates(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("host.json", new URLTemplate("host.json", classLoader.getResource("functions/azure/host.json")));
        generatorContext.addTemplate("local.settings.json", new URLTemplate("local.settings.json", classLoader.getResource("functions/azure/local.settings.json")));
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ApplicationType type = generatorContext.getApplicationType();
        loadTemplates(generatorContext);
        Project project = generatorContext.getProject();
        BuildTool buildTool = generatorContext.getBuildTool();
        if (buildTool.isGradle()) {
            generatorContext.addHelpLink("Azure Functions Plugin for Gradle", "https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions");
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.microsoft.azure.azurefunctions")
                    .lookupArtifactId("azure-functions-gradle-plugin")
                    .extension(new RockerWritable(azurefunctions.template(generatorContext.getProject(), generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY))))
                    .build());
        } else if (buildTool == BuildTool.MAVEN) {
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
        addFunctionTemplate(generatorContext, project);
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

    protected void addFunctionTemplate(GeneratorContext generatorContext, Project project) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            String triggerFile = generatorContext.getSourcePath("/{packagePath}/Function");
            generatorContext.addTemplate("trigger", triggerFile,
                    azureRawFunctionTriggerJava.template(project),
                    azureRawFunctionTriggerKotlin.template(project),
                    azureRawFunctionTriggerGroovy.template(project));
        }
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return azureRawFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return azureRawFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return azureRawFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return azureRawFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return azureRawFunctionSpock.template(project);
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package azure-functions:run";
        } else {
            return "gradlew clean azureFunctionsRun";
        }
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package azure-functions:deploy";
        } else if (buildTool.isGradle()) {
            return "gradlew clean azureFunctionsDeploy";
        } else {
            throw new IllegalStateException("Unsupported build tool");
        }
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        return "azure_function";
    }
}
