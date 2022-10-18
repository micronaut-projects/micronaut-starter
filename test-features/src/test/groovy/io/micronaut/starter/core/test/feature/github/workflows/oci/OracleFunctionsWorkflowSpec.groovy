package io.micronaut.starter.core.test.feature.github.workflows.oci

import com.oracle.bmc.Region
import com.oracle.bmc.apigateway.DeploymentClient
import com.oracle.bmc.apigateway.model.ApiSpecification
import com.oracle.bmc.apigateway.model.ApiSpecificationRoute
import com.oracle.bmc.apigateway.model.CreateDeploymentDetails
import com.oracle.bmc.apigateway.model.Deployment
import com.oracle.bmc.apigateway.model.DeploymentSummary
import com.oracle.bmc.apigateway.model.OracleFunctionBackend
import com.oracle.bmc.apigateway.requests.CreateDeploymentRequest
import com.oracle.bmc.apigateway.requests.DeleteDeploymentRequest
import com.oracle.bmc.apigateway.requests.GetDeploymentRequest
import com.oracle.bmc.apigateway.requests.ListDeploymentsRequest
import com.oracle.bmc.apigateway.responses.CreateDeploymentResponse
import com.oracle.bmc.apigateway.responses.DeleteDeploymentResponse
import com.oracle.bmc.apigateway.responses.GetDeploymentResponse
import com.oracle.bmc.apigateway.responses.ListDeploymentsResponse
import com.oracle.bmc.auth.AuthenticationDetailsProvider
import com.oracle.bmc.functions.FunctionsInvokeClient
import com.oracle.bmc.functions.FunctionsManagementClient
import com.oracle.bmc.functions.model.FunctionSummary
import com.oracle.bmc.functions.requests.InvokeFunctionRequest
import com.oracle.bmc.functions.requests.ListFunctionsRequest
import com.oracle.bmc.functions.responses.InvokeFunctionResponse
import com.oracle.bmc.functions.responses.ListFunctionsResponse
import com.oracle.bmc.util.StreamUtils
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.HttpClient
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.feature.github.workflows.oci.AbstractOracleFunctionsWorkflow
import io.micronaut.starter.feature.github.workflows.oci.OracleFunctionsGraalWorkflow
import io.micronaut.starter.feature.github.workflows.oci.OracleFunctionsJavaWorkflow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.github.WorkflowSpec
import io.micronaut.starter.util.NameUtils
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.stream.Collectors

import static com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider.SimpleAuthenticationDetailsProviderBuilder

/**
 * This integration spec contains tests for {@link OracleFunctionsJavaWorkflow} and {@link OracleFunctionsGraalWorkflow}
 * for both build tools. To run this spec env variable required to be in env which are afterwards automatically created
 * as GitHub Secrets, see {@link OracleFunctionsWorkflowSpec#envVariables()}
 */
@Requires({
    OracleFunctionsWorkflowSpec.envVariables().stream().allMatch { envVar -> System.getenv().containsKey(envVar) } &&  \
      jvm.isJava11()
})
@Slf4j
class OracleFunctionsWorkflowSpec extends WorkflowSpec {

    // env variables for test of http functions
    public static final String OCI_GATEWAY_OCID = "OCI_GATEWAY_OCID"
    public static final String OCI_COMPARTMENT_OCID = "OCI_COMPARTMENT_OCID"

    final AuthenticationDetailsProvider provider = new SimpleAuthenticationDetailsProviderBuilder()
            .region(Region.valueOf(AbstractOracleFunctionsWorkflow.WORKFLOW_DEFAULT_REGION))
            .userId(System.getenv(AbstractOracleFunctionsWorkflow.OCI_USER_OCID))
            .fingerprint(System.getenv(AbstractOracleFunctionsWorkflow.OCI_FINGERPRINT))
            .tenantId(System.getenv(AbstractOracleFunctionsWorkflow.OCI_TENANCY_OCID))
            .privateKeySupplier({
                new ByteArrayInputStream(convertPrivateKeyOnelinerToMultiline(System.getenv(OracleFunctionsJavaWorkflow.OCI_KEY_FILE)).getBytes(StandardCharsets.UTF_8))
            }).build()

    static List<String> envVariables() {
        return Arrays.asList(
                OracleFunctionsJavaWorkflow.OCI_AUTH_TOKEN,
                OracleFunctionsJavaWorkflow.OCI_OCIR_REPOSITORY,
                OracleFunctionsJavaWorkflow.OCI_USER_OCID,
                OracleFunctionsJavaWorkflow.OCI_TENANCY_OCID,
                OracleFunctionsJavaWorkflow.OCI_KEY_FILE,
                OracleFunctionsJavaWorkflow.OCI_FINGERPRINT,
                OracleFunctionsJavaWorkflow.OCI_FUNCTION_APPLICATION_OCID,
                GH_TOKEN
        )
    }

    @Shared
    private List<Secret> secrets

    @Shared
    private BlockingHttpClient httpClient

    @Override
    String getTempDirectoryPrefix() {
        return "OracleFunctionsWorkflowSpec"
    }

    void setupSpec() {
        secrets = envVariables().stream()
                .filter { envVar -> envVar != OracleFunctionsJavaWorkflow.OCI_KEY_FILE }
                .map { envVar -> secretFromEnvVariable(envVar) }
                .collect(Collectors.toList())
        // This is here for development purpose in case the private key file is injected as oneliner, what
        // happens when JIdea configuration is used.
        secrets.add(new Secret(OracleFunctionsJavaWorkflow.OCI_KEY_FILE,
                convertPrivateKeyOnelinerToMultiline(System.getenv(OracleFunctionsJavaWorkflow.OCI_KEY_FILE)),
                null))

        DefaultHttpClientConfiguration configuration = beanContext.getBean(DefaultHttpClientConfiguration.class)
        configuration.setReadTimeout(Duration.ofSeconds(AbstractOracleFunctionsWorkflow.WORKFLOW_DEFAULT_TIMEOUT_IN_SECONDS.toInteger()))

        httpClient = beanContext.createBean(HttpClient.class).toBlocking()
    }

    @Unroll
    void "test #buildTool java oracle raw function workflow"(BuildTool buildTool) {
        given:
        def name = "oci-raw-function-${buildTool.name.toLowerCase()}-java-test"
        def project = NameUtils.parse("com.example.${name}")

        when:
        generateProject(project, Language.JAVA, buildTool, [OracleFunctionsJavaWorkflow.NAME], ApplicationType.FUNCTION)
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeRawFunction(name).contains("Your tenancy is: ${System.getenv(AbstractOracleFunctionsWorkflow.OCI_TENANCY_OCID)}")

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << [BuildTool.MAVEN, BuildTool.GRADLE]
    }

    @Unroll
    void "test #buildTool graalvm oracle raw function workflow"(BuildTool buildTool) {
        given:
        def name = "oci-raw-function-${buildTool.name.toLowerCase()}-graalvm-test"
        def project = NameUtils.parse("com.example.${name}")

        when:
        generateProject(project, Language.JAVA, buildTool, [OracleFunctionsGraalWorkflow.NAME], ApplicationType.FUNCTION)
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeRawFunction(name).contains("Your tenancy is: ${System.getenv(AbstractOracleFunctionsWorkflow.OCI_TENANCY_OCID)}")

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << [BuildTool.MAVEN, BuildTool.GRADLE]
    }

    @Unroll
    void "test #buildTool.name java oracle http function workflow"(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.oci-http-function-${buildTool.name.toLowerCase()}-java-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [OracleFunctionsJavaWorkflow.NAME], ApplicationType.DEFAULT)
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeHttpFunctionIndex(project) == "Example Response"

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << [BuildTool.MAVEN, BuildTool.GRADLE]
    }

    @Unroll
    void "test #buildTool graalvm oracle http function workflow"(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.oci-http-function-${buildTool.name.toLowerCase()}-graalvm-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [OracleFunctionsGraalWorkflow.NAME], ApplicationType.DEFAULT)
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeHttpFunctionIndex(project) == "Example Response"

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << [BuildTool.MAVEN, BuildTool.GRADLE]
    }

    /**
     * Invokes HTTP function trough the API gateway deployment it creates.
     * @param project project
     * @return body of the response
     */
    String invokeHttpFunctionIndex(Project project){
        String endpoint = getOrCreateApiGwDeploymentEndpoint(project.getName(), "/" + project.getPropertyName())
        HttpResponse<String> resp = httpClient.exchange(endpoint + "/", String.class)
        return resp.body()
    }

    /**
     * Creates or gets configured API gateway deploymet for given function name and pathPrefix.
     * @param functionName display name
     * @param pathPrefix deployment route path prefix
     * @return deployment endpoint
     */
    String getOrCreateApiGwDeploymentEndpoint(String functionName, String pathPrefix) {
        String compartmentOcid = System.getenv(OCI_COMPARTMENT_OCID)
        String gatewayOcid = System.getenv(OCI_GATEWAY_OCID)

        DeploymentClient deploymentClient = new DeploymentClient(provider)
        deploymentClient.setRegion(AbstractOracleFunctionsWorkflow.WORKFLOW_DEFAULT_REGION)

        ListDeploymentsRequest listDeploymentsRequest = new ListDeploymentsRequest.Builder()
                .compartmentId(compartmentOcid)
                .gatewayId(gatewayOcid).build()
        ListDeploymentsResponse listDeploymentsResponse = deploymentClient.listDeployments(listDeploymentsRequest)

        DeploymentSummary deploymentSummary = listDeploymentsResponse.deploymentCollection.items
                .find { it.displayName == functionName }

        if (deploymentSummary != null &&
                ( deploymentSummary.lifecycleState == Deployment.LifecycleState.Failed || deploymentSummary.getPathPrefix() != pathPrefix)) {
            DeleteDeploymentRequest deleteDeploymentRequest = new DeleteDeploymentRequest.Builder()
                    .deploymentId(deploymentSummary.getId())
                    .build()
            DeleteDeploymentResponse deleteDeploymentResponse = deploymentClient.deleteDeployment(deleteDeploymentRequest)
        }

        String endpoint
        if(deploymentSummary != null){
            endpoint = deploymentSummary.endpoint
        } else {
            FunctionSummary function = loadFunction(functionName)
            CreateDeploymentDetails createDeploymentDetails = new CreateDeploymentDetails.Builder()
                    .compartmentId(compartmentOcid)
                    .displayName(functionName)
                    .gatewayId(gatewayOcid)
                    .pathPrefix(pathPrefix)
                    .specification(
                            new ApiSpecification.Builder().routes([
                                    new ApiSpecificationRoute.Builder()
                                            .backend(
                                                    new OracleFunctionBackend.Builder()
                                                            .functionId(function.getId())
                                                            .build())
                                            .methods([ApiSpecificationRoute.Methods.Any])
                                            .path("/{path*}")
                                            .build()
                            ]).build()).build()
            CreateDeploymentRequest createDeploymentRequest = new CreateDeploymentRequest.Builder()
                    .createDeploymentDetails(createDeploymentDetails)
                    .build()
            CreateDeploymentResponse createDeploymentResponse = deploymentClient.createDeployment(createDeploymentRequest)
            GetDeploymentRequest getDeploymentRequest = new GetDeploymentRequest.Builder()
                    .deploymentId(createDeploymentResponse.deployment.getId())
                    .build()

            // wait 30 seconds to configure the deployment
            int retries = 30
            while (retries > 0) {
                GetDeploymentResponse response = deploymentClient.getDeployment(getDeploymentRequest)
                Deployment.LifecycleState lifecycleState = response.deployment.lifecycleState
                if (lifecycleState == Deployment.LifecycleState.Creating) {
                    sleep(1000)
                } else if (lifecycleState == Deployment.LifecycleState.Active) {
                    break
                } else {
                    throw new Exception("Failed to create Deployment ${createDeploymentRequest} with status: ${response}")
                }
                retries--
            }
            endpoint = createDeploymentResponse.deployment.getEndpoint()
        }
        return endpoint;
    }

    /**
     * Load OCI function.
     * @param functionName function name
     * @return function
     */
    FunctionSummary loadFunction(String functionName) {
        FunctionsManagementClient fnManagementClient = new FunctionsManagementClient(provider)
        FunctionsInvokeClient fnInvokeClient = new FunctionsInvokeClient(provider)
        try {
            fnManagementClient.setRegion(AbstractOracleFunctionsWorkflow.WORKFLOW_DEFAULT_REGION)

            return getUniqueFunctionByName(fnManagementClient,
                    System.getenv(OracleFunctionsJavaWorkflow.OCI_FUNCTION_APPLICATION_OCID),
                    functionName)
        } finally {
            fnInvokeClient.close()
            fnManagementClient.close()
        }
    }

    /**
     * Invokes raw functions.
     *
     * @param functionName function name
     * @return response
     */
    String invokeRawFunction(String functionName) {
        FunctionsManagementClient fnManagementClient = new FunctionsManagementClient(provider)
        FunctionsInvokeClient fnInvokeClient = new FunctionsInvokeClient(provider)
        try {
            fnManagementClient.setRegion(AbstractOracleFunctionsWorkflow.WORKFLOW_DEFAULT_REGION)
            final FunctionSummary fn = loadFunction(functionName)

            return invokeFunction(fnInvokeClient, fn, "")
        } finally {
            fnInvokeClient.close()
            fnManagementClient.close()
        }
    }

    /**
     * Credits https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/InvokeFunctionExample.java
     */
    static FunctionSummary getUniqueFunctionByName(
            final FunctionsManagementClient fnManagementClient,
            final String applicationId,
            final String functionDisplayName) throws Exception {

        final ListFunctionsRequest listFunctionsRequest =
                ListFunctionsRequest.builder()
                        .applicationId(applicationId)
                        .displayName(functionDisplayName)
                        .build()

        final ListFunctionsResponse listFunctionsResponse =
                fnManagementClient.listFunctions(listFunctionsRequest)

        if (listFunctionsResponse.getItems().size() != 1) {
            throw new Exception(
                    "Could not find function with name " +
                            functionDisplayName +
                            " in application " +
                            applicationId)
        }

        return listFunctionsResponse.getItems().get(0)
    }

    /**
     * Credits https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/InvokeFunctionExample.java
     */
    private static String invokeFunction(
            final FunctionsInvokeClient fnInvokeClient,
            final FunctionSummary function,
            final String payload)
            throws Exception {

        log.info("Invoking function endpoint - " + function.getInvokeEndpoint())

        // Configure the client to use the assigned function endpoint.
        fnInvokeClient.setEndpoint(function.getInvokeEndpoint())
        final InvokeFunctionRequest invokeFunctionRequest =
                InvokeFunctionRequest.builder()
                        .functionId(function.getId())
                        .invokeFunctionBody(StreamUtils.createByteArrayInputStream(payload.getBytes()))
                        .fnIntent(InvokeFunctionRequest.FnIntent.Cloudevent)
                        .build()

        // Invoke the function!
        final InvokeFunctionResponse invokeFunctionResponse = fnInvokeClient.invokeFunction(invokeFunctionRequest)

        // Handle the response.
        invokeFunctionResponse.getInputStream().text
    }
}
