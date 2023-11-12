package io.micronaut.starter.feature.github.workflows.gcloud

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import io.micronaut.starter.util.VersionInfo
import spock.lang.Unroll

class GoogleCloudRunWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github workflow readme'() {
        when:
        def output = generate([GoogleCloudRunGraalWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("Google Cloud Run GraalVM GitHub Workflow")
        readme.contains("GCLOUD_PROJECT_ID")
    }

    @Unroll
    void 'test graalvm github workflow is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [GoogleCloudRunGraalWorkflow.NAME])
        def workflow = output[".github/workflows/google-cloud-run-graalvm.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${GCLOUD_GCR}/\${GCLOUD_PROJECT_ID}/\${GCLOUD_IMAGE_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    @Unroll
    void 'test github workflow is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [GoogleCloudRunJavaWorkflow.NAME])
        println(output)
        def workflow = output[".github/workflows/google-cloud-run.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${GCLOUD_GCR}/\${GCLOUD_PROJECT_ID}/\${GCLOUD_IMAGE_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")

        where:
        buildTool << BuildTool.values()
    }

    void 'test docker image is configured in build.gradle'() {
        when:
        def output = generate([GoogleCloudRunGraalWorkflow.NAME])
        def gradle = output['build.gradle.kts']

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
                [GoogleCloudRunGraalWorkflow.NAME])
        def workflow = output[".github/workflows/google-cloud-run-graalvm.yml"]

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")

        where:
        jdkVersion | graalVersion
        JdkVersion.JDK_17 | JdkVersion.JDK_17
    }

    @Unroll
    void 'test github #buildTool with java #jdkVersion workflow'(BuildTool buildTool, JdkVersion jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, jdkVersion),
                [GoogleCloudRunJavaWorkflow.NAME])
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
