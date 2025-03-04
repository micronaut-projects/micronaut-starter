package io.micronaut.starter.feature.github.workflows.azure

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
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
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([AzureContainerInstanceJavaWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def workflow = output[".github/workflows/azure-container-instance.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_REPOSITORY_PATH}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    void 'test docker image is configured in #buildFileName for #feature'(BuildTool buildTool, String feature) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .buildTool(buildTool)
                .features([feature])
                .build()
        when:
        def output = generate(options)
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
        images = listOf("${System.getenv("DOCKER_IMAGE") ?: project.name}:${project.version}")
    }''')

            assert gradle.contains('''
    dockerBuildNative {
        images = listOf("${System.getenv("DOCKER_IMAGE") ?: project.name}:${project.version}")
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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(jdkVersion)
                .features([AzureContainerInstanceGraalWorkflow.NAME])
                .build()

        when:
        def output = generate(options)
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
