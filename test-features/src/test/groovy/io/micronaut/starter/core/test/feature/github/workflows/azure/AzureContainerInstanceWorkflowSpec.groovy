package io.micronaut.starter.core.test.feature.github.workflows.azure

import com.azure.core.management.AzureEnvironment
import com.azure.core.management.profile.AzureProfile
import com.azure.identity.ClientSecretCredential
import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.resourcemanager.containerinstance.ContainerInstanceManager
import com.azure.resourcemanager.containerinstance.models.ContainerGroup
import groovy.json.JsonSlurper
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.starter.application.Project
import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.feature.github.workflows.azure.AbstractAzureContainerInstanceWorkflow
import io.micronaut.starter.feature.github.workflows.azure.AzureContainerInstanceGraalWorkflow
import io.micronaut.starter.feature.github.workflows.azure.AzureContainerInstanceJavaWorkflow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.github.WorkflowSpec
import io.micronaut.starter.util.NameUtils
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Unroll

import java.util.stream.Collectors

/**
 * This integration spec contains tests for {@link AbstractAzureContainerInstanceWorkflow} for both build tools.
 * To run this spec env variable required to be in env which are afterwards automatically created as GitHub Secrets,
 * see {@link AzureContainerInstanceWorkflowSpec#envVariables()}
 */
@Requires({
    AzureContainerInstanceWorkflowSpec.envVariables().stream().allMatch { envVar -> System.getenv().containsKey(envVar) }
})
class AzureContainerInstanceWorkflowSpec extends WorkflowSpec {

    static List<String> envVariables() {
        return Arrays.asList(
                AbstractAzureContainerInstanceWorkflow.AZURE_RESOURCE_GROUP,
                AbstractAzureContainerInstanceWorkflow.DOCKER_PASSWORD,
                AbstractAzureContainerInstanceWorkflow.DOCKER_REGISTRY_URL,
                AbstractAzureContainerInstanceWorkflow.DOCKER_REPOSITORY_PATH,
                AbstractAzureContainerInstanceWorkflow.DOCKER_USERNAME,
                AbstractAzureContainerInstanceWorkflow.AZURE_CREDENTIALS,
                GH_TOKEN
        )
    }

    @Shared
    private List<Secret> secrets

    @Shared
    private BlockingHttpClient httpClient

    @Override
    String getTempDirectoryPrefix() {
        return "AzureContainerInstanceWorkflowSpec"
    }

    void setupSpec() {
        secrets = envVariables().stream()
                .map { envVar -> secretFromEnvVariable(envVar) }
                .collect(Collectors.toList())

        httpClient = beanContext.createBean(HttpClient.class).toBlocking()
    }

    void "test azure container instance #buildTool graalvm workflow "(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.azure-container-instance-${buildTool.name.toLowerCase()}-graalvm-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [AzureContainerInstanceGraalWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeRestApi(project) == "Example Response"

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << BuildToolCombinations.buildTools
    }

    void "test azure container instance #buildTool workflow"(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.azure-container-instance-${buildTool.name.toLowerCase()}-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [AzureContainerInstanceJavaWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 15 * 60)

        then:
        noExceptionThrown()
        invokeRestApi(project) == "Example Response"

        cleanup:
        cleanupGitHubRepository(project)

        where:
        buildTool << BuildToolCombinations.buildTools
    }

    /**
     * Invoke the container rest api endpoint
     *
     * @param project project
     * @return body response
     */
    String invokeRestApi(Project project){

        String credentials = System.getenv(AbstractAzureContainerInstanceWorkflow.AZURE_CREDENTIALS)
        JsonSlurper slurper =  new JsonSlurper()
        Map credentialsJson = slurper.parseText(credentials)

        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(credentialsJson.clientId)
                .clientSecret(credentialsJson.clientSecret)
                .tenantId(credentialsJson.tenantId)
                .build()

        AzureProfile profile = new AzureProfile(credentialsJson.tenantId,
                credentialsJson.subscriptionId,
                AzureEnvironment.AZURE)
        ContainerInstanceManager manager = ContainerInstanceManager.authenticate(clientSecretCredential, profile);

        ContainerGroup containerGroup = manager.containerGroups().list().find {it.name() == project.getName()}
        String invokeUrl = containerGroup.innerModel().ipAddress().fqdn()

        HttpResponse<String> resp = httpClient.exchange(
                "http://${invokeUrl}:${AbstractAzureContainerInstanceWorkflow.AZURE_DEFAULT_WORKFLOW_PORT}/${project.getPropertyName()}", String.class)
        return resp.body()
    }
}
