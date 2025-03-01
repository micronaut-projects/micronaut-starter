package io.micronaut.starter.feature.ci.workflows.github

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.ci.workflows.gcp.GoogleCloudCiWorkflowFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Unroll

class GithubWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test github-workflow-ci is created for #buildTool and #jdkVersion'(BuildTool buildTool, int jdkVersion, String workflowName) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(JdkVersion.valueOf(jdkVersion))
                .features([GithubCiWorkflowFeature.NAME])
                .build()

        Map<String, String> output = generate(options)
        String workflow = output[".github/workflows/${workflowName}"]

        then:
        workflow
        workflow.contains("name: Java CI with ${buildTool.title}")
        workflow.contains("uses: actions/checkout@v3")
        workflow.contains("uses: actions/setup-java@v3")
        workflow.contains("java-version: ${jdkVersion}")

        where:
        buildTool               | jdkVersion | workflowName
        BuildTool.GRADLE        | 17         | "gradle.yml"
        BuildTool.GRADLE_KOTLIN | 17         | "gradle.yml"
        BuildTool.MAVEN         | 17         | "maven.yml"
    }

    @Unroll
    void 'test github-workflow-ci wrapper validation and upload is created for #buildTool'(BuildTool buildTool) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([GithubCiWorkflowFeature.NAME])
                .build()

        def output = generate(options)
        def workflow = output[".github/workflows/gradle.yml"]

        then:
        workflow
        workflow.contains("uses: gradle/wrapper-validation-action@v1.0.4")
        workflow.contains("gradle/gradle-build-action@v2.2.0")
        workflow.contains("actions/upload-artifact@v3.1.0")

        where:
        buildTool << BuildTool.valuesGradle()
    }
}
