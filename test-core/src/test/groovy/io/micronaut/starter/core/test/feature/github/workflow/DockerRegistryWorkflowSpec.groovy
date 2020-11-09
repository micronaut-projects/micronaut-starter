package io.micronaut.starter.core.test.feature.github.workflow

import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.feature.github.workflows.docker.DockerRegistryWorkflow
import io.micronaut.starter.feature.github.workflows.docker.GraalVMDockerRegistryWorkflow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.github.WorkflowSpec
import io.micronaut.starter.util.NameUtils
import spock.lang.Requires
import spock.lang.Shared

/**
 * This integration spec contains tests for both {@link DockerRegistryWorkflow} and {@link GraalVMDockerRegistryWorkflow}.
 * To run this spec env variable required to be in env which are afterwards automatically created as GitHub Secrets:
 * <ul>
 *     <li>DOCKER_USERNAME - Username used to authenticate to Docker registry</li>
 *     <li>DOCKER_PASSWORD - Password used to authenticate to Docker registry</li>
 *     <li>DOCKER_REGISTRY_URL - Docker registry url to authenticate aga repository path.</li>
 *     <li>DOCKER_ORGANIZATION - Docker organisation repository path.</li>
 * </ul>
 */
@Requires({System.getenv().containsKey(GH_TOKEN) && System.getenv().containsKey(DOCKER_USERNAME) \
        && System.getenv().containsKey(DOCKER_PASSWORD) && System.getenv().containsKey(DOCKER_ORGANIZATION)})
class DockerRegistryWorkflowSpec extends WorkflowSpec {

    private static final String DOCKER_USERNAME = "DOCKER_USERNAME"
    private static final String DOCKER_PASSWORD = "DOCKER_PASSWORD"
    private static final String DOCKER_REGISTRY_URL = "DOCKER_REGISTRY_URL"
    private static final String DOCKER_ORGANIZATION = "DOCKER_ORGANIZATION"

    @Shared
    private List<Secret> secrets

    @Override
    String getTempDirectoryPrefix() {
        return "DockerRegistryWorkflowSpec"
    }

    void setupSpec() {
        secrets = Arrays.asList(
                secretFromEnvVariable(DOCKER_USERNAME),
                secretFromEnvVariable(DOCKER_PASSWORD),
                secretFromEnvVariable(DOCKER_ORGANIZATION),
                secretFromEnvVariable(DOCKER_REGISTRY_URL)
        )
    }

    void "test gradle workflow"() {
        given:
        def project = NameUtils.parse("com.example.starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.GRADLE, [DockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 3*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }

    void "test maven workflow"() {
        given:
        def project = NameUtils.parse("com.example.starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.MAVEN, [DockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 10*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }


    void "test graalvm gradle workflow"() {
        given:
        def project = NameUtils.parse("com.example.graalvm-starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.GRADLE, [GraalVMDockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 15*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }

    void "test graalvm maven workflow"() {
        given:
        def project = NameUtils.parse("com.example.graalvm-starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.MAVEN, [GraalVMDockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 15*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }
}
