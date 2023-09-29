package io.micronaut.starter.feature.github.workflows.docker

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import io.micronaut.starter.util.VersionInfo
import spock.lang.Unroll

class GraalVMDockerRegistryWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github workflow readme'() {
        when:
        def output = generate([GraalVMDockerRegistryWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("""
Add the following GitHub secrets:

| Name | Description |
| ---- | ----------- |
| DOCKER_USERNAME | Username for Docker registry authentication. |
| DOCKER_PASSWORD | Docker registry password. |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL | Docker registry url. |
""")
    }

    @Unroll
    void 'test github workflow is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [GraalVMDockerRegistryWorkflow.NAME])
        def workflow = output[".github/workflows/graalvm.yml"]

        then:
        workflow

        where:
        buildTool << BuildTool.values()
    }

    void 'test docker image configured in build.gradle'() {
        when:
        def output = generate([GraalVMDockerRegistryWorkflow.NAME])
        def gradle = output['build.gradle.kts']

        then:
        gradle
        gradle.contains("""
    dockerBuildNative {
        images = [\"\${System.env.DOCKER_IMAGE ?: project.name}:\$project.version"]
    }""")
    }

    void 'test push to docker workflow for maven'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [GraalVMDockerRegistryWorkflow.NAME])
        def maven = output['.github/workflows/graalvm.yml']

        then:
        maven
        maven.contains("export DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_REPOSITORY_PATH}/foo")
    }

    @Unroll
    void 'test github gradle graal #graalVersion workflow for #jdkVersion'(JdkVersion jdkVersion,
                                                                           JdkVersion graalVersion) {
        given:
        def graalvmVersion = "${VersionInfo.getDependencyVersion('graal').getValue()}" +
                ".java${graalVersion.majorVersion()}"

        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [GraalVMDockerRegistryWorkflow.NAME])
        def workflow = output['.github/workflows/graalvm.yml']

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")

        where:
        jdkVersion        | graalVersion
        JdkVersion.JDK_17 | JdkVersion.JDK_17
    }
}
