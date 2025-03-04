package io.micronaut.starter.feature.github.workflows.gcloud

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

class GoogleCloudRunWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github workflow readme'() {
        when:
        Map<String, String> output = generate([GoogleCloudRunGraalWorkflow.NAME])
        String readme = output['README.md']

        then:
        readme
        readme.contains("Google Cloud Run GraalVM GitHub Workflow")
        readme.contains("GCLOUD_PROJECT_ID")
    }

    void 'test graalvm github workflow is created for #buildTool'(BuildTool buildTool) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([GoogleCloudRunGraalWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def workflow = output[".github/workflows/google-cloud-run-graalvm.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${GCLOUD_GCR}/\${GCLOUD_PROJECT_ID}/\${GCLOUD_IMAGE_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    void 'test github workflow is created for #buildTool'(BuildTool buildTool) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([GoogleCloudRunJavaWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        println(output)
        def workflow = output[".github/workflows/google-cloud-run.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${GCLOUD_GCR}/\${GCLOUD_PROJECT_ID}/\${GCLOUD_IMAGE_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    void 'test docker image is configured in #buildFileName'(BuildTool buildTool) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .buildTool(buildTool)
                .features([GoogleCloudRunGraalWorkflow.NAME])
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
        buildTool << BuildTool.valuesGradle()
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
                .features([GoogleCloudRunGraalWorkflow.NAME])
                .build()

        when:
        def output = generate(options)
        def workflow = output[".github/workflows/google-cloud-run-graalvm.yml"]

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")

        where:
        jdkVersion | graalVersion
        JdkVersion.JDK_17 | JdkVersion.JDK_17
    }

    void 'test github #buildTool with java #jdkVersion workflow'(BuildTool buildTool, JdkVersion jdkVersion) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(jdkVersion)
                .features([GoogleCloudRunJavaWorkflow.NAME])
                .build()
        when:
        def output = generate(options)
        def workflow = output['.github/workflows/google-cloud-run.yml']

        then:
        workflow
        workflow.contains("java-version: ${jdkVersion.majorVersion()}")

        where:
        [buildTool, jdkVersion] << [
                [BuildTool.GRADLE, BuildTool.MAVEN],
                MicronautJdkVersionConfiguration.SUPPORTED_JDKS
        ].combinations()
    }
}
