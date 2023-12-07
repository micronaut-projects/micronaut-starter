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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.chatbots.ChatBotsFeature;
import io.micronaut.starter.feature.function.gcp.template.gcpFunctionReadme;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionGroovy;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionJava;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionKotlin;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawFunctionGroovyJunit;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawFunctionJavaJunit;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawFunctionKoTest;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawFunctionKotlinJunit;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawFunctionSpock;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class GoogleCloudRawFunction extends AbstractGoogleCloudFunction {
    public static final String NAME = "google-cloud-function";

    private static final Dependency MICRONAUT_GCP_FUNCTION = MicronautDependencyUtils
            .gcpDependency()
            .artifactId("micronaut-gcp-function")
            .compile()
            .build();

    private final GoogleCloudFunction googleCloudFunction;

    public GoogleCloudRawFunction(GoogleCloudFunction googleCloudFunction, ShadePlugin shadePlugin, JacksonDatabindFeature jacksonDatabindFeature) {
        super(shadePlugin, jacksonDatabindFeature);
        this.googleCloudFunction = googleCloudFunction;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ApplicationType type = generatorContext.getApplicationType();
        if (type == ApplicationType.FUNCTION && generatorContext.isFeatureMissing(CodeContributingFeature.class)) {
            Project project = generatorContext.getProject();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/Function");
            generatorContext.addTemplate(
                    "function",
                    sourceFile,
                    gcpRawBackgroundFunctionJava.template(project),
                    gcpRawBackgroundFunctionKotlin.template(project),
                    gcpRawBackgroundFunctionGroovy.template(project)
            );

            applyTestTemplate(generatorContext, project, "Function");
            addDependencies(generatorContext);
        }
    }

    void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_GCP_FUNCTION);
        generatorContext.addDependency(GCP_FUNCTIONS_FRAMEWORK_API.compileOnly());
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addDependency(GCP_FUNCTIONS_FRAMEWORK_API.test());
        }
    }

    @Override
    public String getTitle() {
        return "Google Cloud Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to deploy to Google Cloud Function";
    }

    @Override
    protected Optional<RockerModel> readmeTemplate(
            GeneratorContext generatorContext,
            Project project,
            BuildTool buildTool) {
        return Optional.of(
            gcpFunctionReadme.template(
                project,
                generatorContext.getFeatures(),
                getRunCommand(buildTool),
                getBuildCommand(buildTool),
                generatorContext.getApplicationType() == ApplicationType.FUNCTION
            )
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                    googleCloudFunction
            );
        }
    }

    @Override
    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            super.applyTestTemplate(generatorContext, project, name);
        }
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        return googleCloudFunction.getRunCommand(buildTool);
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return googleCloudFunction.getBuildCommand(buildTool);
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return gcpRawFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return gcpRawFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return gcpRawFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return gcpRawFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return gcpRawFunctionSpock.template(project);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions";
    }
}
