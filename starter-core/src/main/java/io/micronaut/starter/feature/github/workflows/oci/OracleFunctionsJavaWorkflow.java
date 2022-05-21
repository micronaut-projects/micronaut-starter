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
package io.micronaut.starter.feature.github.workflows.oci;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction;

import jakarta.inject.Singleton;

/**
 * Deploy dockerized application to Oracle Functions from Oracle Container Registry.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
@Singleton
public class OracleFunctionsJavaWorkflow extends AbstractOracleFunctionsWorkflow {

    public static final String NAME = "github-workflow-oracle-cloud-functions";

    public OracleFunctionsJavaWorkflow(OracleRawFunction oracleRawFunction) {
        super(oracleRawFunction, false);
    }

    @Override
    public String getTitle() {
        return "Oracle Functions GitHub Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to deploy to Oracle Functions from Oracle Cloud Infrastructure Registry";
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return "oracle-cloud-functions.yml";
    }
}
