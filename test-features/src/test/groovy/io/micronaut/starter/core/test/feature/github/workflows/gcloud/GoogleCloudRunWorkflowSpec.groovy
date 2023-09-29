package io.micronaut.starter.core.test.feature.github.workflows.gcloud

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.run.v1.CloudRun
import com.google.api.services.run.v1.model.ListServicesResponse
import com.google.api.services.run.v1.model.Service
import com.google.auth.Credentials
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.starter.application.Project
import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.feature.github.workflows.gcloud.GoogleCloudRunGraalWorkflow
import io.micronaut.starter.feature.github.workflows.gcloud.GoogleCloudRunJavaWorkflow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.github.WorkflowSpec
import io.micronaut.starter.util.NameUtils
import spock.lang.Requires
import spock.lang.Shared

import java.util.stream.Collectors

/**
 * This integration spec contains tests for {@link GoogleCloudRunJavaWorkflow} and {@link GoogleCloudRunGraalWorkflow}
 * for both build tools. To run this spec env variables are required to be in configured which are afterwards automatically
 * created as GitHub Secrets. For required variables see see {@link GoogleCloudRunWorkflowSpec#envVariables()}
 */
@Requires({
    GoogleCloudRunWorkflowSpec.envVariables().stream().allMatch { envVar -> System.getenv().containsKey(envVar) }
})

class GoogleCloudRunWorkflowSpec extends WorkflowSpec {
    static List<String> envVariables() {
        return Arrays.asList(
                GH_TOKEN,
                GoogleCloudRunGraalWorkflow.GCLOUD_PROJECT_ID,
                GoogleCloudRunGraalWorkflow.GCLOUD_SA_KEY
        )
    }

    @Shared
    private List<Secret> secrets

    @Shared
    private BlockingHttpClient httpClient

    @Override
    String getTempDirectoryPrefix() {
        return "GoogleCloudRunWorkflowSpec"
    }

    void setupSpec() {
        secrets = envVariables().stream()
                .map { envVar -> secretFromEnvVariable(envVar) }
                .collect(Collectors.toList())

        httpClient = beanContext.createBean(HttpClient.class).toBlocking()
    }

    void "test google cloud run graalvm #buildTool workflow"(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.google-cloud-run-${buildTool.name.toLowerCase()}-graalvm-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [GoogleCloudRunGraalWorkflow.NAME])
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

    void "test google cloud run #buildTool workflow"(BuildTool buildTool) {
        given:
        def project = NameUtils.parse("com.example.google-cloud-run-${buildTool.name.toLowerCase()}-test")

        when:
        generateProject(project, Language.JAVA, buildTool, [GoogleCloudRunJavaWorkflow.NAME])
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

    private String invokeRestApi(Project project){
        String sa = System.getenv(GoogleCloudRunGraalWorkflow.GCLOUD_SA_KEY)
        Credentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(sa.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform")

        CloudRun cloudRun = new CloudRun(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))

        CloudRun.Namespaces.Services.List list = cloudRun.namespaces()
                .services()
                .list("namespaces/${System.getenv(GoogleCloudRunJavaWorkflow.GCLOUD_PROJECT_ID)}")
        ListServicesResponse listServicesResponse = list.execute()

        Service service = listServicesResponse.getItems().find {
            it.getMetadata().getName() == project.name}

        String invokeUrl = service.getStatus().getUrl()

        HttpResponse<String> resp = httpClient.exchange("${invokeUrl}/${project.getPropertyName()}", String.class)
        return resp.body()
    }
}
