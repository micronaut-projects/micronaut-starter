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

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.FunctionFeatureCodeGenerator;
import io.micronaut.starter.feature.function.oraclefunction.template.projectFnFunc;
import io.micronaut.starter.feature.logging.Logback;
import io.micronaut.starter.feature.logging.SimpleLogging;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
@Primary
public class OracleFunction extends AbstractFunctionFeature implements OracleCloudFeature, FunctionFeature {

    private final SimpleLogging simpleLogging;

    public OracleFunction(SimpleLogging simpleLogging) {
        this.simpleLogging = simpleLogging;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(SimpleLogging.class)) {
            featureContext.addFeature(simpleLogging);
            featureContext.exclude(feature -> feature instanceof Logback);
        }

        if (featureContext.isPresent(ServerFeature.class)) {
            featureContext.exclude(feature -> feature instanceof ServerFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addTemplate(
                "func.yml", new RockerTemplate(
                        "func.yml",
                        projectFnFunc.template(generatorContext.getProject()
                ))
        );
    }

    @Override
    @NonNull
    public String getName() {
        return "oracle-function-http";
    }

    @Override
    public String getTitle() {
        return "Oracle Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to Oracle Cloud Function";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String getTestSuffix(ApplicationType type) {
        if (type == ApplicationType.FUNCTION) {
            return "Function";
        }
        return "Controller";
    }

    @Override
    protected FunctionFeatureCodeGenerator resolveFunctionFeatureCodeGenerator(GeneratorContext generatorContext) {
        return new DefaultOracleFunctionFeatureCodeGenerator();
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#httpFunctions";
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        return "oracle_function";
    }
}
