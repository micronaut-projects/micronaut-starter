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
package io.micronaut.starter.feature.function.oraclefunction;

import com.fizzed.rocker.RockerModel;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.oraclefunction.template.raw.*;
import io.micronaut.starter.feature.logging.SimpleLogging;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class OracleRawFunction extends OracleFunction {
    private final OracleFunction httpFunction;

    public OracleRawFunction(SimpleLogging simpleLogging, OracleFunction httpFunction) {
        super(simpleLogging);
        this.httpFunction = httpFunction;
    }

    @Override
    public String getName() {
        return "oracle-function";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                    httpFunction
            );
        }
        super.processSelectedFeatures(featureContext);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType type = generatorContext.getApplicationType();
        if (type == ApplicationType.FUNCTION) {
            applyFunction(generatorContext, type);
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

            applyTestTemplate(generatorContext, project, "Function");
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
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#functions";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm";
    }
}
