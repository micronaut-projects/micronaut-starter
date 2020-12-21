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
package io.micronaut.starter.feature.github.workflows.oci;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction;

import javax.inject.Singleton;

/**
 * Deploy GraalVM dockerized application to Oracle Functions from Oracle Container Registry.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
@Singleton
public class OracleFunctionsGraalWorkflow extends AbstractOracleFunctionsWorkflow {

    public static final String NAME = "github-workflow-oracle-oci-functions-graalvm";

    public OracleFunctionsGraalWorkflow(OracleRawFunction oracleRawFunction) {
        super(oracleRawFunction, true);
    }

    @Override
    public String getTitle() {
        return "Oracle Functions GraalVM GitHub Workflow";
    }

    @Override
    public String getDescription() {
        return "Adds GitHub workflow that deploys GraalVM native image to Oracle Functions from Oracle Cloud Infrastructure Registry.";
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return "oci-functions-graalvm.yml";
    }
}
