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
package io.micronaut.starter.feature.function.gcp;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.FunctionFeatureCodeGenerator;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionGroovy;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionJava;
import io.micronaut.starter.feature.function.gcp.template.raw.gcpRawBackgroundFunctionKotlin;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
public class GoogleCloudFunction extends AbstractFunctionFeature implements EntryPointFeature, GcpMicronautRuntimeFeature {
    private static final String MICRONAUT_DOCS_GCP_SIMPLE_FUNCTION = "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions";
    private static final String MICRONAUT_DOCS_GCP_HTTP_FUNCTION = "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#httpFunctions";

    private static final String GROUP_ID_GOOGLE_CLOUD_FUNCTIONS = "com.google.cloud.functions";
    private static final String ARTIFACT_ID_FUNCTIONS_FRAMEWORK_API = "functions-framework-api";
    private static final String ARTIFACT_ID_MICRONAUT_GCP_FUNCTION = "micronaut-gcp-function";
    private static final Dependency DEPENDENCY_MICRONAUT_GCP_FUNCTION = MicronautDependencyUtils.gcpDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_GCP_FUNCTION)
            .compile()
            .build();

    private static final String NAME = "google-cloud-function";

    private String micronautDocs;

    private final ShadePlugin shadePlugin;
    private final GcpReadmeFeature gcpReadmeFeature;

    protected GoogleCloudFunction(ShadePlugin shadePlugin,
                                  GcpReadmeFeature gcpReadmeFeature) {
        this.shadePlugin = shadePlugin;
        this.gcpReadmeFeature = gcpReadmeFeature;
    }

    @Override
    public String getMicronautDocumentation() {
        return micronautDocs;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Cloud Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to Google Cloud Function";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ApplicationType type = generatorContext.getApplicationType();
        micronautDocs = type == ApplicationType.DEFAULT ?
                MICRONAUT_DOCS_GCP_HTTP_FUNCTION :
                MICRONAUT_DOCS_GCP_SIMPLE_FUNCTION;
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
        addDependencies(generatorContext);
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_GCP_FUNCTION);
            generatorContext.addDependency(functionFrameworksApiDependency().test());
            generatorContext.addDependency(functionFrameworksApiDependency().compileOnly());
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
        featureContext.addFeatureIfNotPresent(GcpReadmeFeature.class, gcpReadmeFeature);
    }

    @Override
    @NonNull
    protected FunctionFeatureCodeGenerator resolveFunctionFeatureCodeGenerator(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getApplicationType() == ApplicationType.DEFAULT ?
                new DefaultGcpFunctionFeatureCodeGenerator() :
                new FunctionGcpFunctionFeatureCodeGenerator();
    }

    @Override
    @NonNull
    public String entryPoint(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getApplicationType() == ApplicationType.FUNCTION) {
            Project project = generatorContext.getProject();
            if (project.getPackageName() != null) {
                return project.getPackageName() + ".Function";
            }
        }
        return "io.micronaut.gcp.function.http.HttpFunction";
    }

    public static Dependency.Builder functionFrameworksApiDependency() {
        return Dependency.builder().groupId(GROUP_ID_GOOGLE_CLOUD_FUNCTIONS).artifactId(ARTIFACT_ID_FUNCTIONS_FRAMEWORK_API);
    }
}
