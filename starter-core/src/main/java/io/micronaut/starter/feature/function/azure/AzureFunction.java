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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.FunctionFeatureCodeGenerator;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerGroovy;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerJava;
import io.micronaut.starter.feature.function.azure.template.azureFunctionTriggerKotlin;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionHttpRequestJava;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionResponseBuilderJava;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerGroovy;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerJava;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionTriggerKotlin;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
public class AzureFunction extends AbstractFunctionFeature implements AzureCloudFeature, FunctionFeature, AzureMicronautRuntimeFeature {
    private static final String MICRONAUT_DOCS_AZURE_HTTP_FUNCTIONS =  "https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#azureHttpFunctions";
    private static final String MICRONAUT_DOCS_AZURE_SIMPLE_AZURE_FUNCTIONS = "https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions";
    private static final String NAME = "azure-function";

    private final AzureBuildPluginFeature azureBuildPluginFeature;
    private String micronautDocsUrl;

    public AzureFunction(AzureBuildPluginFeature azureBuildPluginFeature) {
        this.azureBuildPluginFeature = azureBuildPluginFeature;
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
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to Microsoft Azure";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(AzureBuildPluginFeature.class, azureBuildPluginFeature);
        featureContext.exclude(ShadePlugin.class::isInstance);
    }

    @Override
    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        micronautDocsUrl = generatorContext.getApplicationType() == ApplicationType.DEFAULT ?
                MICRONAUT_DOCS_AZURE_HTTP_FUNCTIONS : MICRONAUT_DOCS_AZURE_SIMPLE_AZURE_FUNCTIONS;
        super.applyFunction(generatorContext, type);
        if (type == ApplicationType.FUNCTION && !(generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.KOTLIN)) {
            generateCode(generatorContext);
        }
    }

    protected void generateCode(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        generateJavaTestClass(generatorContext,
                "HttpRequestTemplate",
                "HttpRequest",
                azureRawFunctionHttpRequestJava.template(project));

        generateJavaTestClass(generatorContext,
                "ResponseBuilderTemplate",
                "ResponseBuilder",
                azureRawFunctionResponseBuilderJava.template(project));

        String testSource = generatorContext.getTestSourcePath("/{packagePath}/Function");

        FunctionFeatureCodeGenerator functionFeatureCodeGenerator = resolveFunctionFeatureCodeGenerator(generatorContext);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(functionFeatureCodeGenerator.spockTemplate(project),
                functionFeatureCodeGenerator.javaJUnitTemplate(project),
                functionFeatureCodeGenerator.groovyJUnitTemplate(project),
                functionFeatureCodeGenerator.kotlinJUnitTemplate(project),
                functionFeatureCodeGenerator.koTestTemplate(project));
        generatorContext.addTemplate("testFunction", testSource, provider);
    }

    @Override
    @NonNull
    protected FunctionFeatureCodeGenerator resolveFunctionFeatureCodeGenerator(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getApplicationType() == ApplicationType.DEFAULT ?
            new DefaultAzureFunctionFeatureCodeGenerator() :
            new FunctionAzureFunctionFeatureCodeGenerator();
    }

    private void generateJavaTestClass(GeneratorContext generatorContext,
                                       String templateName,
                                       String name,
                                       RockerModel javaModel) {
        String  testSource = Language.JAVA.getTestSrcDir() + "/{packagePath}/" + name + "." + Language.JAVA.getExtension();
        generatorContext.addTemplate(templateName, new RockerTemplate(testSource, javaModel));
    }

    @Override
    public String getMicronautDocumentation() {
        return micronautDocsUrl;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.microsoft.com/azure";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        Project project = generatorContext.getProject();
        addFunctionTemplate(generatorContext, project);
    }

    protected void addFunctionTemplate(GeneratorContext generatorContext, Project project) {
        if (generatorContext.getApplicationType() == ApplicationType.DEFAULT) {
            String triggerFile = generatorContext.getSourcePath("/{packagePath}/Function");
            generatorContext.addTemplate("trigger", triggerFile,
                    azureFunctionTriggerJava.template(project),
                    azureFunctionTriggerKotlin.template(project),
                    azureFunctionTriggerGroovy.template(project));
        } else if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            String triggerFile = generatorContext.getSourcePath("/{packagePath}/Function");
            generatorContext.addTemplate("trigger", triggerFile,
                    azureRawFunctionTriggerJava.template(project),
                    azureRawFunctionTriggerKotlin.template(project),
                    azureRawFunctionTriggerGroovy.template(project));
        }
    }
}
