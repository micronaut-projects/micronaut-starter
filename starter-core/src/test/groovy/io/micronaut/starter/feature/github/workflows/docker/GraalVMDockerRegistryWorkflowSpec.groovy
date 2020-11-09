package io.micronaut.starter.feature.github.workflows.docker

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import io.micronaut.starter.util.VersionInfo
import spock.lang.Requires
import spock.lang.Unroll

@Requires({ jvm.isJava8() || jvm.isJava11() })
class GraalVMDockerRegistryWorkflowSpec extends BeanContextSpec implements CommandOutputFixture{

    void 'test github workflow readme'(){
        when:
        def output = generate([GraalVMDockerRegistryWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("""
The GitHub secrets in table below needs to be configured:

| Name | Description |
| ---- | ----------- |
| DOCKER_USERNAME | Username for Docker registry authentication. |
| DOCKER_PASSWORD | Docker registry password. |
| DOCKER_ORGANIZATION | Path to the docker image registry, e.g. for image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL | Docker registry url. |
""")
    }

    @Unroll
    void 'test github workflow is created for #buildTool'(BuildTool buildTool, String workflowName) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_11),
                [GraalVMDockerRegistryWorkflow.NAME])
        def workflow = output[".github/workflows/${workflowName}"]

        then:
        workflow

        where:
        buildTool | workflowName
        BuildTool.GRADLE | "gradle-graalvm.yml"
        BuildTool.MAVEN | "maven-graalvm.yml"
    }

    void 'test docker image configured in build.gradle'() {
        when:
        def output = generate([GraalVMDockerRegistryWorkflow.NAME])
        def gradle = output['build.gradle']

        then:
        gradle
        gradle.contains("""
    dockerBuildNative{
        images = [\"\${System.env.DOCKER_IMAGE}:\$project.version"]
    }""")
    }

    void 'test push to docker workflow for maven'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11),
                [GraalVMDockerRegistryWorkflow.NAME])
        def maven = output['.github/workflows/maven-graalvm.yml']

        then:
        maven
        maven.contains("DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_ORGANIZATION}/foo")
    }

    @Unroll
    void 'test github gradle graal #graalVersion workflow for #jdkVersion'(JdkVersion jdkVersion,
                                                                           JdkVersion graalVersion){
        given:
        def graalvmVersion = "${VersionInfo.getDependencyVersion( 'graal').getValue()}" +
                ".java${graalVersion.majorVersion()}"

        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [GraalVMDockerRegistryWorkflow.NAME])
        def workflow = output['.github/workflows/gradle-graalvm.yml']

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")

        where:
        jdkVersion | graalVersion
        JdkVersion.JDK_8  | JdkVersion.JDK_8
        JdkVersion.JDK_9  | JdkVersion.JDK_11
        JdkVersion.JDK_10 | JdkVersion.JDK_11
        JdkVersion.JDK_11 | JdkVersion.JDK_11
    }
}
