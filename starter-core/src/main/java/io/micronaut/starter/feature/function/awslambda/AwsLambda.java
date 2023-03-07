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
package io.micronaut.starter.feature.function.awslambda;

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.architecture.Arm;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.AwsApiFeature;
import io.micronaut.starter.feature.aws.AwsLambdaEventFeature;
import io.micronaut.starter.feature.aws.AwsLambdaSnapstart;
import io.micronaut.starter.feature.awsalexa.AwsAlexa;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.function.Cloud;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.DocumentationLink;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.starter.feature.function.awslambda.template.*;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.feature.security.SecurityFeature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.options.TestRockerModelProvider;
import io.micronaut.starter.template.RockerWritable;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;

import static io.micronaut.starter.application.ApplicationType.DEFAULT;
import static io.micronaut.starter.application.ApplicationType.FUNCTION;

@Singleton
public class AwsLambda implements FunctionFeature, DefaultFeature, CloudFeature, HandlerClassFeature, MicronautRuntimeFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";
    public static final String MICRONAUT_LAMBDA_HANDLER = "io.micronaut.function.aws.proxy.MicronautLambdaHandler";
    public static final String REQUEST_HANDLER = "FunctionRequestHandler";
    private static final String LINK_TITLE = "AWS Lambda Handler";
    private static final String LINK_URL = "https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html";
    private static final Dependency AWS_LAMBDA_JAVA_EVENTS = Dependency.builder().lookupArtifactId("aws-lambda-java-events").compile().build();

    private final ShadePlugin shadePlugin;
    private final AwsLambdaCustomRuntime customRuntime;
    private final CpuArchitecture defaultCpuArchitecture;
    private final AwsLambdaSnapstart snapstart;

    @Deprecated
    public AwsLambda(ShadePlugin shadePlugin,
                     AwsLambdaCustomRuntime customRuntime) {
        this(shadePlugin, customRuntime, new X86(), new AwsLambdaSnapstart());
    }

    @Deprecated
    public AwsLambda(ShadePlugin shadePlugin,
                     AwsLambdaCustomRuntime customRuntime,
                     Arm arm) {
        this.shadePlugin = shadePlugin;
        this.customRuntime = customRuntime;
        this.defaultCpuArchitecture = arm;
        this.snapstart = new AwsLambdaSnapstart();
    }

    @Deprecated
    public AwsLambda(ShadePlugin shadePlugin,
                     AwsLambdaCustomRuntime customRuntime,
                     X86 x86) {
        this(shadePlugin, customRuntime, x86, new AwsLambdaSnapstart());
    }

    @Inject
    public AwsLambda(ShadePlugin shadePlugin,
                     AwsLambdaCustomRuntime customRuntime,
                     X86 x86,
                     AwsLambdaSnapstart snapstart) {
        this.shadePlugin = shadePlugin;
        this.customRuntime = customRuntime;
        this.defaultCpuArchitecture = x86;
        this.snapstart = snapstart;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
        featureContext.addFeatureIfNotPresent(CpuArchitecture.class, defaultCpuArchitecture);
        if (featureContext.isPresent(GraalVM.class) &&
                (
                        featureContext.getBuildTool() == BuildTool.MAVEN ||
                        (featureContext.getBuildTool().isGradle() && featureContext.getApplicationType() == FUNCTION)
                )
        ) {
            featureContext.addFeature(customRuntime);
        }

        if (shouldAddSnapstartFeature(featureContext)) {
            featureContext.addFeature(snapstart);
        }
    }

    protected boolean shouldAddSnapstartFeature(FeatureContext featureContext) {
        if (featureContext.isPresent(GraalVM.class)) {
            return false;
        }
        return featureContext.getFeature(CpuArchitecture.class)
                .filter(CpuArchitecture.class::isInstance)
                .map(CpuArchitecture.class::cast)
                .map(snapstart::supports)
                .orElse(true);
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to AWS Lambda";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (shouldGenerateSources(generatorContext)) {
            ApplicationType applicationType = generatorContext.getApplicationType();
            if (applicationType == DEFAULT || applicationType == FUNCTION) {
                addCode(generatorContext);
                if (applicationType == FUNCTION) {
                    generatorContext.addDependency(AWS_LAMBDA_JAVA_EVENTS);
                }
                addHelpTemplate(generatorContext);
                disableSecurityFilterInTestConfiguration(generatorContext);
            }
        }
        generatorContext.getBuildProperties().put(PROPERTY_MICRONAUT_RUNTIME, resolveMicronautRuntime(generatorContext));
    }

    protected void addCode(@NonNull GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        ApplicationType applicationType = generatorContext.getApplicationType();
        if (applicationType == DEFAULT) {
            addHomeController(generatorContext, project);
            addHomeControllerTest(generatorContext, project);
        } else if (applicationType == FUNCTION) {
            addRequestHandler(generatorContext, project);
            if (generatorContext.getFeatures().hasFeature(AwsApiFeature.class) ||
                    !generatorContext.getFeatures().hasFeature(AwsLambdaEventFeature.class)) {
                addTest(generatorContext, project);
            }
        }
    }

    protected void addHelpTemplate(@NonNull GeneratorContext generatorContext) {
        DocumentationLink link = new DocumentationLink(LINK_TITLE, LINK_URL);
        generatorContext.addHelpTemplate(new RockerWritable(readmeRockerModel(this, generatorContext, link)));
    }

    protected void disableSecurityFilterInTestConfiguration(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().hasFeature(SecurityFeature.class)) {
            ApplicationConfiguration test = generatorContext.getConfiguration(Environment.FUNCTION, ApplicationConfiguration.functionTestConfig());
            test.put("micronaut.security.filter.enabled", false);
        }
    }

    boolean shouldGenerateSources(GeneratorContext generatorContext) {
        return !generatorContext.getFeatures().isFeaturePresent(AwsAlexa.class);
    }

    private void addHomeControllerTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/HomeController");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(homeControllerSpock.template(project),
                homeControllerJavaJunit.template(project),
                homeControllerGroovyJunit.template(project),
                homeControllerKotlinJunit.template(project),
                homeControllerKoTest.template(project));
        generatorContext.addTemplate("testHomeController", testSource, provider);
    }

    private void addHomeController(GeneratorContext generatorContext, Project project) {
        String controllerFile = generatorContext.getSourcePath("/{packagePath}/HomeController");
        generatorContext.addTemplate("homeController", controllerFile,
                homeControllerJava.template(project),
                homeControllerKotlin.template(project),
                homeControllerGroovy.template(project));
    }

    private void addTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/FunctionRequestHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(awsLambdaFunctionRequestHandlerSpock.template(project),
                awsLambdaFunctionRequestHandlerJavaJunit.template(project),
                awsLambdaFunctionRequestHandlerGroovyJunit.template(project),
                awsLambdaFunctionRequestHandlerKotlinJunit.template(project),
                awsLambdaFunctionRequestHandlerKoTest.template(project));
        generatorContext.addTemplate("testFunctionRequestHandler", testSource, provider);
    }

    private void addRequestHandler(GeneratorContext generatorContext, Project project) {
        String awsLambdaRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/" + REQUEST_HANDLER);
        generatorContext.addTemplate("functionRequestHandler", awsLambdaRequestHandlerFile,
                awsLambdaFunctionRequestHandlerJava.template(generatorContext.getFeatures(), project),
                awsLambdaFunctionRequestHandlerKotlin.template(generatorContext.getFeatures(), project),
                awsLambdaFunctionRequestHandlerGroovy.template(generatorContext.getFeatures(), project));
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == FUNCTION &&
                selectedFeatures.stream().filter(feature -> feature instanceof CloudFeature)
                        .noneMatch(cloudFeature -> ((CloudFeature) cloudFeature).getCloud() != getCloud());
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }

    @Override
    public Cloud getCloud() {
        return Cloud.AWS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda";
    }

    @Override
    public String handlerClass(ApplicationType applicationType, Project project) {
        switch (applicationType) {
            case DEFAULT:
                return MICRONAUT_LAMBDA_HANDLER;
            case FUNCTION:
                return project.getPackageName() + "." + REQUEST_HANDLER;
            default:
                return null;
        }
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return "lambda";
        }
        return generatorContext.getFeatures().contains("graalvm") ? "lambda_provided" : "lambda_java";
    }
}

