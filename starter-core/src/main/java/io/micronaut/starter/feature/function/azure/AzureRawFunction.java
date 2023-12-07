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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.azure.template.azureFunctionReadme;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionHttpRequestJava;
import io.micronaut.starter.feature.function.azure.template.raw.azureRawFunctionResponseBuilderJava;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class AzureRawFunction extends AbstractAzureFunction {
    private static final Dependency MICRONAUT_AZURE_FUNCTION = MicronautDependencyUtils
            .azureDependency()
            .artifactId("micronaut-azure-function")
            .compile()
            .build();
    private final AzureHttpFunction httpFunction;

    public AzureRawFunction(CoordinateResolver coordinateResolver, AzureHttpFunction httpFunction) {
        super(coordinateResolver);
        this.httpFunction = httpFunction;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.exclude(ShadePlugin.class::isInstance);
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(httpFunction);
        }
    }

    @Override
    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            super.applyTestTemplate(generatorContext, project, name);
        }
    }

    @Override
    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        super.applyFunction(generatorContext, type);

        if (type == ApplicationType.FUNCTION
                && generatorContext.isFeatureMissing(CodeContributingFeature.class)
                && !(generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.KOTLIN)) {
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
            TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spockTemplate(project),
                    javaJUnitTemplate(project),
                    groovyJUnitTemplate(project),
                    kotlinJUnitTemplate(project),
                    koTestTemplate(project));
            generatorContext.addTemplate("testFunction", testSource, provider);
        }
    }

    private void generateJavaTestClass(GeneratorContext generatorContext,
                                       String templateName,
                                       String name,
                                       RockerModel javaModel) {
        String  testSource = Language.JAVA.getTestSrcDir() + "/{packagePath}/" + name + "." + Language.JAVA.getExtension();
        generatorContext.addTemplate(templateName, new RockerTemplate(testSource, javaModel));
    }

    @Override
    protected Optional<RockerModel> readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool) {
        return Optional.of(
                azureFunctionReadme.template(
                        project,
                        generatorContext.getFeatures(),
                        getRunCommand(buildTool),
                        getBuildCommand(buildTool),
                        buildTool)
        );
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.microsoft.com/azure";
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        super.addDependencies(generatorContext);
        generatorContext.addDependency(MICRONAUT_AZURE_FUNCTION);
    }
}
