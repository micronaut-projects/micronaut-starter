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
package io.micronaut.starter.feature.function.azure;

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.azure.template.azureFunctionReadme;
import io.micronaut.starter.feature.function.azure.template.raw.*;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.URLTemplate;

/**
 * Function impl for Azure.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractAzureFunction extends AbstractFunctionFeature implements CloudFeature {

    public static final String NAME = "azure-function";

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
        return "Adds support for writing functions deployable to Microsoft Azure";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ApplicationType type = generatorContext.getApplicationType();
        generatorContext.addTemplate("host.json", new URLTemplate("host.json", classLoader.getResource("functions/azure/host.json")));
        generatorContext.addTemplate("local.settings.json", new URLTemplate("local.settings.json", classLoader.getResource("functions/azure/local.settings.json")));
        BuildTool buildTool = generatorContext.getBuildTool();
        Project project = generatorContext.getProject();
        if (buildTool == BuildTool.MAVEN) {
            BuildProperties props = generatorContext.getBuildProperties();
            props.put(
                    "functionAppName",
                    project.getName()
            );
            props.put(
                    "functionAppRegion",
                    "westus"
            );
            props.put(
                    "functionResourceGroup",
                    "java-functions-group"
            );
            props.put(
                    "stagingDirectory",
                    "${project.build.directory}/azure-functions/${functionAppName}"
            );
        }

        addFunctionTemplate(generatorContext, project);
        applyFunction(generatorContext, type);
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
    protected RockerModel readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool) {
        return azureFunctionReadme.template(project,
                generatorContext.getFeatures(),
                getRunCommand(buildTool),
                getBuildCommand(buildTool)
        );
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
    public Cloud getCloud() {
        return Cloud.AZURE;
    }

}
