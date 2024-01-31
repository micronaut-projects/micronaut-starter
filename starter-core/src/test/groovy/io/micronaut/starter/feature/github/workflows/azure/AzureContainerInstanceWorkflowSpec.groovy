package io.micronaut.starter.feature.github.workflows.azure

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo

class AzureContainerInstanceWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github java workflow readme'() {
        when:
        Map<String, String> output = generate([AzureContainerInstanceJavaWorkflow.NAME])
        String readme = output['README.md']

        then:
        readme
        readme.contains("Azure Container Instance Workflow")
    }

    void 'test github graalvm workflow readme'() {
        when:
        Map<String, String> output = generate([AzureContainerInstanceGraalWorkflow.NAME])
        String readme = output['README.md']

        then:
        readme
        readme.contains("Azure Container Instance GraalVM Workflow")
    }

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

    void 'test docker image is configured in #buildFileName for #feature'(BuildTool buildTool, String feature) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), [feature])
        def gradle = output[buildTool.buildFileName]

        then:
        if (buildTool == BuildTool.GRADLE) {
            assert gradle.contains('''
    dockerBuild {
        images = ["${System.env.DOCKER_IMAGE ?: project.name}:$project.version"]
    }''')

            assert gradle.contains('''
    dockerBuildNative {
        images = ["${System.env.DOCKER_IMAGE ?: project.name}:$project.version"]
    }''')
        } else {
            assert gradle.contains('''
    dockerBuild {
        images.set(listOf("${System.getenv("DOCKER_IMAGE") ?: project.name}:${project.version}"))
    }''')

            assert gradle.contains('''
    dockerBuildNative {
        images.set(listOf("${System.getenv("DOCKER_IMAGE") ?: project.name}:${project.version}"))
    }''')
        }

        where:
        [buildTool, feature] << [BuildTool.valuesGradle(), [AzureContainerInstanceJavaWorkflow.NAME, AzureContainerInstanceGraalWorkflow.NAME]].combinations()
        buildFileName = buildTool.buildFileName
    }

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
