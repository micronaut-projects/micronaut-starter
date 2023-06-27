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
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.azure.template.azureFunctionGroovyJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionJavaJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionKoTest;
import io.micronaut.starter.feature.function.azure.template.azureFunctionKotlinJunit;
import io.micronaut.starter.feature.function.azure.template.azureFunctionSpock;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerGroovy;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerJava;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerKotlin;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class AzureHttpFunction extends AbstractAzureFunction implements Feature {

    private static final Dependency MICRONAUT_AZURE_FUNCTION_HTTP = MicronautDependencyUtils
            .azureDependency()
            .artifactId("micronaut-azure-function-http")
            .compile()
            .build();

    private static final Dependency MICRONAUT_AZURE_FUNCTION_HTTP_TEST = MicronautDependencyUtils
            .azureDependency()
            .artifactId("micronaut-azure-function-http-test")
            .test()
            .build();

    public static final String NAME = "azure-function-http";

    public AzureHttpFunction(CoordinateResolver coordinateResolver) {
        super(coordinateResolver);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return azureFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return azureFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return azureFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return azureFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return azureFunctionSpock.template(project);
    }

    @Override
    protected void addFunctionTemplate(GeneratorContext generatorContext, Project project) {
        if (generatorContext.getApplicationType() == ApplicationType.DEFAULT) {
            String triggerFile = generatorContext.getSourcePath("/{packagePath}/Function");
            generatorContext.addTemplate("trigger", triggerFile,
                    azureFunctionTriggerJava.template(project),
                    azureFunctionTriggerKotlin.template(project),
                    azureFunctionTriggerGroovy.template(project));
        }

    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#azureHttpFunctions";
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        super.addDependencies(generatorContext);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(MICRONAUT_AZURE_FUNCTION_HTTP);
            generatorContext.addDependency(MICRONAUT_AZURE_FUNCTION_HTTP_TEST);
        }
    }
}
