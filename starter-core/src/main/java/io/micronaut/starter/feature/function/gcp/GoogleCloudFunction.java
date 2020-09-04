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
import io.micronaut.starter.feature.function.gcp.template.*;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;

/**
 * A feature for supporting Google Cloud Function.
 *
 * @author graemerocher
 * @since 2.0.0
 */
@Singleton
public class GoogleCloudFunction extends AbstractFunctionFeature implements CloudFeature {

    public static final String NAME = "google-cloud-function-http";

    private final ShadePlugin shadePlugin;

    public GoogleCloudFunction(ShadePlugin shadePlugin) {
        this.shadePlugin = shadePlugin;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(ShadePlugin.class)) {
            featureContext.addFeature(shadePlugin);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
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
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        applyFunction(generatorContext, generatorContext.getApplicationType());
    }

    @Override
    protected RockerModel readmeTemplate(
            GeneratorContext generatorContext,
            Project project,
            BuildTool buildTool) {
        return gcpFunctionReadme.template(project,
                generatorContext.getFeatures(),
                getRunCommand(buildTool),
                getBuildCommand(buildTool),
                generatorContext.getApplicationType() == ApplicationType.FUNCTION
        );
    }

    @Override
    public RockerModel javaJUnitTemplate(Project project) {
        return gcpFunctionJavaJunit.template(project);
    }

    @Override
    public RockerModel kotlinJUnitTemplate(Project project) {
        return gcpFunctionKotlinJunit.template(project);
    }

    @Override
    public RockerModel groovyJUnitTemplate(Project project) {
        return gcpFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return gcpFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return gcpFunctionSpock.template(project);
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw function:run";
        } else {
            return "gradlew runFunction";
        }
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else {
            return "gradlew clean shadowJar";
        }
    }

    @Override
    public Cloud getCloud() {
        return Cloud.GCP;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/snapshot/guide/index.html#cloudFunction";
    }
}
