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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction;
import io.micronaut.starter.feature.github.workflows.Secret;
import io.micronaut.starter.feature.github.workflows.docker.AbstractDockerRegistryWorkflow;
import io.micronaut.starter.feature.github.workflows.oci.templates.ociFunctionsWorkflow;
import io.micronaut.starter.feature.github.workflows.oci.templates.ociFunctionsWorkflowReadme;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;

import java.util.Arrays;
import java.util.List;

/**
 * Deploy dockerized app to Oracle Functions
 *
 * @author Pavol Gressa
 * @since 2.3
 */

public abstract class AbstractOracleFunctionsWorkflow extends AbstractDockerRegistryWorkflow {

    // Secrets
    public static final String OCI_AUTH_TOKEN = "OCI_AUTH_TOKEN";
    public static final String OCI_OCIR_REPOSITORY = "OCI_OCIR_REPOSITORY";
    public static final String OCI_USER_OCID = "OCI_USER_OCID";
    public static final String OCI_TENANCY_OCID = "OCI_TENANCY_OCID";
    public static final String OCI_KEY_FILE = "OCI_KEY_FILE";
    public static final String OCI_FINGERPRINT = "OCI_FINGERPRINT";
    public static final String OCI_PASSPHRASE = "OCI_PASSPHRASE";
    public static final String OCI_FUNCTION_APPLICATION_OCID = "OCI_FUNCTION_APPLICATION_OCID";

    // Workflow defaults
    public static final String WORKFLOW_DEFAULT_REGION = "us-ashburn-1";
    public static final String WORKFLOW_DEFAULT_OCIR_URL = "iad.ocir.io";
    public static final String WORKFLOW_DEFAULT_MEMORY_IN_MBS = "128";
    public static final String WORKFLOW_DEFAULT_TIMEOUT_IN_SECONDS = "120";

    private final OracleRawFunction oracleRawFunction;
    private boolean isGraal;

    public AbstractOracleFunctionsWorkflow(OracleRawFunction oracleRawFunction, boolean isGraal) {
        this.oracleRawFunction = oracleRawFunction;
        this.isGraal = isGraal;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.github.com/en/free-pro-team@latest/actions";
    }

    @Override
    public List<Secret> getSecrets() {
        return Arrays.asList(

                new Secret(OCI_AUTH_TOKEN, "OCI account auth token."),
                new Secret(OCI_OCIR_REPOSITORY, "(Optional) Docker image repository in OCIR. For image `iad.ocir.io/[tenancy name]/foo/bar:0.1`, the `foo` is an _image repository_."),

                // OCI CLI related
                new Secret(OCI_USER_OCID, "OCI user ocid."),
                new Secret(OCI_TENANCY_OCID, "OCID of the tenancy."),
                new Secret(OCI_KEY_FILE, "OCI api signing private key file. See more on [Setup of API signing key](https://docs.cloud.oracle.com/en-us/iaas/Content/Functions/Tasks/functionssetupapikey.htm)."),
                new Secret(OCI_FINGERPRINT, "OCI Api signing key file fingerprint."),
                new Secret(OCI_PASSPHRASE, "Passphrase to the private key file. Required only when passphrase is needed by the private key file."),

                // OCI Functions related
                new Secret(OCI_FUNCTION_APPLICATION_OCID, "Oracle function application OCID. See more on [Creating Applications](https://docs.cloud.oracle.com/en-us/iaas/Content/Functions/Tasks/functionscreatingapps.htm).")
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(OracleRawFunction.class)) {
            featureContext.addFeature(oracleRawFunction);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        if (generatorContext.getBuildTool().equals(BuildTool.MAVEN) && generatorContext.getApplicationType().equals(ApplicationType.FUNCTION)) {
            generatorContext.getBuildProperties().put("exec.mainClass", generatorContext.getProject().getPackageName() + ".Function");
        }

        String workflowFilePath = ".github/workflows/" + getWorkflowFileName(generatorContext);
        generatorContext.addTemplate("ociFunctionsWorkflow",
                new RockerTemplate(workflowFilePath,
                        ociFunctionsWorkflow.template(generatorContext.getProject(), generatorContext.getBuildTool(),
                                generatorContext.getJdkVersion(), isGraal)
                )
        );
        generatorContext.addHelpTemplate(new RockerWritable(ociFunctionsWorkflowReadme.template(
                this, generatorContext.getProject(), generatorContext.getApplicationType(), workflowFilePath)));
    }
}
