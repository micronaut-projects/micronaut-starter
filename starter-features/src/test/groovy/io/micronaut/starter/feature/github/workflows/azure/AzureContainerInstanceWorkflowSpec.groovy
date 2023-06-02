package io.micronaut.starter.feature.github.workflows.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import io.micronaut.starter.util.VersionInfo
import spock.lang.Unroll

class AzureContainerInstanceWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github java workflow readme'() {
        when:
        def output = generate([AzureContainerInstanceJavaWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("Azure Container Instance Workflow")
    }

    void 'test github graalvm workflow readme'() {
        when:
        def output = generate([AzureContainerInstanceGraalWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("Azure Container Instance GraalVM Workflow")
    }

    @Unroll
    void 'test java github workflow is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [AzureContainerInstanceJavaWorkflow.NAME])
        def workflow = output[".github/workflows/azure-container-instance.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_REPOSITORY_PATH}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    @Unroll
    void 'test docker image is configured in build.gradle for #name'(String name) {
        when:
        def output = generate([name])
        def gradle = output['build.gradle']

        then:
        gradle
        gradle.contains("""
    dockerBuild {
        images = [\"\${System.env.DOCKER_IMAGE ?: project.name}:\$project.version"]
    }""")

        gradle.contains("""
    dockerBuildNative {
        images = [\"\${System.env.DOCKER_IMAGE ?: project.name}:\$project.version"]
    }""")

        where:
        name << [AzureContainerInstanceJavaWorkflow.NAME, AzureContainerInstanceGraalWorkflow.NAME]
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
                [AzureContainerInstanceGraalWorkflow.NAME])
        def workflow = output['.github/workflows/azure-container-instance-graalvm.yml']

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")
        workflow.contains("export DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_REPOSITORY_PATH}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        jdkVersion | graalVersion
        JdkVersion.JDK_17 | JdkVersion.JDK_17
    }
}
