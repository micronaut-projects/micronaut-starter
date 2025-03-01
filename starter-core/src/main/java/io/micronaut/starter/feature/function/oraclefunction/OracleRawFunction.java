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
package io.micronaut.starter.feature.function.oraclefunction;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionGroovy;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionGroovyJunit;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionGroovySpock;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionJava;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionJavaJunit;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionKotlin;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionKotlinJunit;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.oracleRawFunctionKotlinKoTest;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;
import io.micronaut.starter.feature.logging.SimpleLogging;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.oracle.function.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Requires(property = "micronaut.starter.feature.oracle.function.http.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OracleRawFunction extends OracleFunction {
    public static final String FEATURE_NAME_ORACLE_RAW_FUNCTION = "oracle-function";

    private static final Dependency MICRONAUT_OCI_FUNCTION = MicronautDependencyUtils
            .ociDependency()
            .artifactId("micronaut-oraclecloud-function")
            .compile()
            .build();
    private static final Dependency COM_FNPROJECT_API = Dependency.builder()
            .groupId(GROUP_ID_COM_FNPROJECT_FN)
            .artifactId("api")
            .compile()
            .build();

    private static final Dependency COM_FNPROJECT_TESTING_JUNIT4 = Dependency.builder()
            .groupId(GROUP_ID_COM_FNPROJECT_FN)
            .artifactId("testing-junit4")
            .test()
            .build();

    private final OracleFunction httpFunction;
    private final JacksonDatabindFeature jacksonDatabindFeature;

    public OracleRawFunction(SimpleLogging simpleLogging,
                             OracleFunction httpFunction,
                             JacksonDatabindFeature jacksonDatabindFeature) {
        super(simpleLogging);
        this.httpFunction = httpFunction;
        this.jacksonDatabindFeature = jacksonDatabindFeature;
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_ORACLE_RAW_FUNCTION;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getOptions() instanceof MicronautOptions mnOptions && mnOptions.applicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                    httpFunction
            );
        }
        super.processSelectedFeatures(featureContext);
        // Requires Jackson due to https://github.com/micronaut-projects/micronaut-oracle-cloud/issues/603
        featureContext.addFeatureIfNotPresent(JacksonDatabindFeature.class, jacksonDatabindFeature);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType type = generatorContext.getOptions() instanceof MicronautOptions mnOptions ? mnOptions.applicationType() : null;
        if (type == ApplicationType.FUNCTION) {
            applyFunction(generatorContext,
                    type);
            Language language = generatorContext.getLanguage();
            Project project = generatorContext.getProject();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/Function");
            switch (language) {
                case GROOVY:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            oracleRawFunctionGroovy.template(project)));
                    break;
                case KOTLIN:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            oracleRawFunctionKotlin.template(project)));
                    break;
                case JAVA:
                default:
                    generatorContext.addTemplate("function", new RockerTemplate(
                            sourceFile,
                            oracleRawFunctionJava.template(project)));
            }

            if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
                addMicronautRuntimeBuildProperty(generatorContext);
                generatorContext.getBuildProperties().put("jib.docker.tag", "${project.version}");
                generatorContext.getBuildProperties().put("exec.mainClass", "com.fnproject.fn.runtime.EntryPoint");
                generatorContext.getBuildProperties().put("jib.docker.image", "[REGION].ocir.io/[TENANCY]/[REPO]/${project.artifactId}");
                generatorContext.getBuildProperties().put("function.entrypoint", project.getPackageName() + ".Function::handleRequest");
            }

            applyTestTemplate(generatorContext, project, "Function");
        }
        addDependencies(generatorContext);
    }

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        if (generatorContext.getOptions() instanceof MicronautOptions micronautOptions && micronautOptions.applicationType() == ApplicationType.FUNCTION) {
            generatorContext.addDependency(MICRONAUT_OCI_FUNCTION);
            generatorContext.addDependency(COM_FNPROJECT_RUNTIME);
            generatorContext.addDependency(COM_FNPROJECT_API);
            generatorContext.addDependency(COM_FNPROJECT_TESTING_JUNIT4);
        }
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return oracleRawFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return oracleRawFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return oracleRawFunctionKotlinJunit.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return oracleRawFunctionGroovySpock.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return oracleRawFunctionKotlinKoTest.template(project);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Nullable
    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#functions";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm";
    }
}
