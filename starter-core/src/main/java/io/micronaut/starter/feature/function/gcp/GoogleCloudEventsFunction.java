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

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.function.gcp.template.cloudevents.*;
import io.micronaut.starter.feature.function.gcp.template.gcpFunctionReadme;
import io.micronaut.starter.feature.other.ShadePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;
import java.util.Optional;


/**
 * A feature for supporting Google CloudEvents Function.
 *
 * @author Guillermo Calvo
 * @since 3.9.0
 */
@Singleton
public class GoogleCloudEventsFunction extends AbstractGoogleCloudFunction {

    public static final String NAME = "google-cloud-function-cloudevents";

    private static final String MICRONAUT_GCP_FUNCTION_CLOUDEVENTS = "micronaut-gcp-function-cloudevents";
    private static final String MICRONAUT_SERDE_API = "micronaut-serde-api";

    private final GoogleCloudFunction googleCloudFunction;

    public GoogleCloudEventsFunction(GoogleCloudFunction googleCloudFunction, ShadePlugin shadePlugin) {
        super(shadePlugin);
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
        Project project = generatorContext.getProject();
        String sourceFile = generatorContext.getSourcePath("/{packagePath}/Function");
        generatorContext.addTemplate("function", new RockerTemplate(sourceFile, sourceFileModel(generatorContext)));

        generatorContext.addDependency(MicronautDependencyUtils.gcpDependency()
            .compile()
            .artifactId(MICRONAUT_GCP_FUNCTION_CLOUDEVENTS));
        generatorContext.addDependency(MicronautDependencyUtils.serdeDependency()
            .compile()
            .artifactId(MICRONAUT_SERDE_API));

        applyTestTemplate(generatorContext, project, "Function");
    }

    @Override
    public String getTitle() {
        return "Google CloudEvent Function";
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
                true
            )
        );
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.FUNCTION;
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
        return gcpCloudEventsFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return gcpCloudEventsFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return gcpCloudEventsFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return gcpCloudEventsFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return gcpCloudEventsFunctionSpock.template(project);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#cloudEventsFunctions";
    }

    @NonNull
    private RockerModel sourceFileModel(GeneratorContext generatorContext) {
        final Language language = generatorContext.getLanguage();
        final Project project = generatorContext.getProject();
        switch (language) {
            case GROOVY:
                return gcpCloudEventsFunctionGroovy.template(project);
            case KOTLIN:
                return gcpCloudEventsFunctionKotlin.template(project);
            case JAVA:
            default:
                return gcpCloudEventsFunctionJava.template(project);
        }
    }
}
