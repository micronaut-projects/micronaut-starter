package io.micronaut.starter.core.test.feature.github.workflows.docker

import io.micronaut.starter.feature.github.workflows.Secret
import io.micronaut.starter.feature.github.workflows.docker.AbstractDockerRegistryWorkflow
import io.micronaut.starter.feature.github.workflows.docker.DockerRegistryWorkflow
import io.micronaut.starter.feature.github.workflows.docker.GraalVMDockerRegistryWorkflow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolTest
import io.micronaut.starter.test.github.WorkflowSpec
import io.micronaut.starter.util.NameUtils
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Shared

import java.util.stream.Collectors

/**
 * This integration spec contains tests for both {@link DockerRegistryWorkflow} and {@link GraalVMDockerRegistryWorkflow}.
 * To run this spec env variable required to be in env which are afterwards automatically created as GitHub Secrets:
 * <ul>
 *     <li>DOCKER_USERNAME - Username used to authenticate to Docker registry</li>
 *     <li>DOCKER_PASSWORD - Password used to authenticate to Docker registry</li>
 *     <li>DOCKER_REGISTRY_URL - Docker registry url to authenticate aga repository path.</li>
 *     <li>DOCKER_REPOSITORY_PATH - Docker repository path.</li>
 * </ul>
 */
@Requires({
    DockerRegistryWorkflowSpec.envVariables().stream().allMatch { envVar -> System.getenv().containsKey(envVar) } \
    && jvm.isJava11()})
class DockerRegistryWorkflowSpec extends WorkflowSpec {

    @Shared
    private List<Secret> secrets

    @Override
    String getTempDirectoryPrefix() {
        return "DockerRegistryWorkflowSpec"
    }

    static List<String> envVariables() {
        return Arrays.asList(
                AbstractDockerRegistryWorkflow.DOCKER_USERNAME,
                AbstractDockerRegistryWorkflow.DOCKER_PASSWORD,
                AbstractDockerRegistryWorkflow.DOCKER_REGISTRY_URL,
                AbstractDockerRegistryWorkflow.DOCKER_REPOSITORY_PATH,
                GH_TOKEN
        )
    }

    void setupSpec() {
        secrets = envVariables().stream()
                .map { envVar -> secretFromEnvVariable(envVar) }
                .collect(Collectors.toList())
    }

    void "test gradle workflow"() {
        given:
        def project = NameUtils.parse("com.example.gradle-java-starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.GRADLE, [DockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 3*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test maven workflow"() {
        given:
        def project = NameUtils.parse("com.example.maven-java-starter-test")

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
        def project = NameUtils.parse("com.example.gradle-graalvm-starter-test")

        when:
        generateProject(project, Language.JAVA, BuildTool.GRADLE, [GraalVMDockerRegistryWorkflow.NAME])
        pushToGitHub(project, secrets)
        workflowPassed(project, 15*60)

        then:
        noExceptionThrown()

        cleanup:
        cleanupGitHubRepository(project)
    }

    @IgnoreIf({ BuildToolTest.IGNORE_MAVEN })
    void "test graalvm maven workflow"() {
        given:
        def project = NameUtils.parse("com.example.maven-graalvm-starter-test")

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
