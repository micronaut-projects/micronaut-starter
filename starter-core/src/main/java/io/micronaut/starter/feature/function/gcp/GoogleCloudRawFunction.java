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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.gcp.template.raw.*;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class GoogleCloudRawFunction extends AbstractFunctionFeature implements CloudFeature {
    public static final String NAME = "google-cloud-function";

    private final GoogleCloudFunction googleCloudFunction;
    private final ShadePlugin shadePlugin;

    public GoogleCloudRawFunction(GoogleCloudFunction googleCloudFunction, ShadePlugin shadePlugin) {
        this.googleCloudFunction = googleCloudFunction;
        this.shadePlugin = shadePlugin;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType type = generatorContext.getApplicationType();
        applyFunction(generatorContext, type);
        if (type == ApplicationType.FUNCTION) {
            Language language = generatorContext.getLanguage();
            Project project = generatorContext.getProject();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/Function");
            switch (language) {
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            gcpRawBackgroundFunctionGroovy.template(project)));
                break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            gcpRawBackgroundFunctionKotlin.template(project)));
                break;
                case JAVA:
                default:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            gcpRawBackgroundFunctionJava.template(project)));
            }

            applyTestTemplate(generatorContext, project, "Function");
        }
    }

    @Override
    public String getTitle() {
        return "Google Cloud Function Support";
    }

    @Override
    public String getDescription() {
        return "Adds Support for Google Cloud Function (https://cloud.google.com/functions)";
    }

    @Override
    protected RockerModel readmeTemplate(
            GeneratorContext generatorContext,
            Project project,
            BuildTool buildTool) {
        return googleCloudFunction.readmeTemplate(generatorContext, project, buildTool);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                    googleCloudFunction
            );
        }

        if (!featureContext.isPresent(ShadePlugin.class)) {
            featureContext.addFeature(shadePlugin);
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
    public Cloud getCloud() {
        return Cloud.GCP;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/snapshot/guide/index.html#simpleFunctions";
    }
}
