package io.micronaut.starter.feature.github.workflows.oci

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

class OracleFunctionsWorkflowSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test github workflow readme'() {
        when:
        def output = generate([OracleFunctionsJavaWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("Oracle Functions GitHub Workflow")
    }

    void 'test github graalvm workflow readme'() {
        when:
        def output = generate([OracleFunctionsGraalWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("Oracle Functions GraalVM GitHub Workflow")
    }

    void 'test github workflow is created for #buildTool with #jdkVersion'(BuildTool buildTool, JdkVersion jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, jdkVersion),
                [OracleFunctionsJavaWorkflow.NAME])
        def workflow = output[".github/workflows/oracle-cloud-functions.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${OCI_OCIR_URL}/\${TENANCY}/\${OCI_OCIR_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")
        workflow.contains("OCI_REGION: ${OracleFunctionsJavaWorkflow.WORKFLOW_DEFAULT_REGION}")
        workflow.contains("OCI_FUNCTION_MEMORY_IN_MBS: ${OracleFunctionsJavaWorkflow.WORKFLOW_DEFAULT_MEMORY_IN_MBS}")
        workflow.contains("OCI_FUNCTION_TIMEOUT_IN_SECONDS: ${OracleFunctionsJavaWorkflow.WORKFLOW_DEFAULT_TIMEOUT_IN_SECONDS}")
        workflow.contains("java-version: ${jdkVersion.majorVersion()}")

        where:
        [buildTool, jdkVersion] << [
                [BuildTool.GRADLE, BuildTool.MAVEN],
                MicronautJdkVersionConfiguration.SUPPORTED_JDKS
        ].combinations()
    }

    void 'test github graalvm workflow is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [OracleFunctionsGraalWorkflow.NAME])
        def workflow = output[".github/workflows/oracle-cloud-functions-graalvm.yml"]

        then:
        workflow
        workflow.contains("export DOCKER_IMAGE=`echo \"\${OCI_OCIR_URL}/\${TENANCY}/\${OCI_OCIR_REPOSITORY}/foo\" | sed -e 's#//#/#' -e 's#^/##'`")
        workflow.contains("OCI_REGION: ${OracleFunctionsGraalWorkflow.WORKFLOW_DEFAULT_REGION}")

        where:
        buildTool << BuildTool.values()
    }

    void 'test http function pom.xml configuration for #feature'(String feature) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [OracleFunctionsJavaWorkflow.NAME])
        def pom = output["pom.xml"]

        then:
        pom
        pom.contains("<artifactId>micronaut-maven-plugin</artifactId>")
        pom.contains("""
      <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <configuration>
          <to>
            <image>\${jib.docker.image}:\${jib.docker.tag}</image>
          </to>
        </configuration>
      </plugin>
""")
        where:
        feature << [OracleFunctionsJavaWorkflow.NAME, OracleFunctionsGraalWorkflow.NAME]
    }

    void 'test graalvm http function pom.xml configuration'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                [OracleFunctionsGraalWorkflow.NAME])
        def pom = output["pom.xml"]

        then:
        pom
        pom.contains("<artifactId>micronaut-maven-plugin</artifactId>")
        pom.contains("""
      <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <configuration>
          <to>
            <image>\${jib.docker.image}:\${jib.docker.tag}</image>
          </to>
        </configuration>
      </plugin>
""")
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
        [buildTool, feature] << [BuildTool.valuesGradle(), [OracleFunctionsJavaWorkflow.NAME, OracleFunctionsGraalWorkflow.NAME]].combinations()
        buildFileName = buildTool.buildFileName
    }

    void 'test github gradle graal #graalVersion workflow for #jdkVersion'(JdkVersion jdkVersion,
                                                                           JdkVersion graalVersion) {
        given:
        def graalvmVersion = "${VersionInfo.getDependencyVersion('graal').getValue()}" +
                ".java${graalVersion.majorVersion()}"

        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [OracleFunctionsGraalWorkflow.NAME])
        def workflow = output['.github/workflows/oracle-cloud-functions-graalvm.yml']

        then:
        workflow
        workflow.contains("graalvm-version: ${graalvmVersion}")

        where:
        jdkVersion        | graalVersion
        JdkVersion.JDK_17 | JdkVersion.JDK_17
    }
}
