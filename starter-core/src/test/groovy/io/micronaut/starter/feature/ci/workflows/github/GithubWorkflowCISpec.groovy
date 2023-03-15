package io.micronaut.starter.feature.ci.workflows.github

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class GithubWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test github-workflow-ci is created for #buildTool and #jdkVersion'(BuildTool buildTool, int jdkVersion, String workflowName) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.valueOf(jdkVersion)),
                [GithubCiWorkflowFeature.NAME])
        def workflow = output[".github/workflows/${workflowName}"]

        then:
        workflow
        workflow.contains("name: Java CI with ${buildTool.title}")
        workflow.contains("uses: actions/checkout@v3")
        workflow.contains("uses: actions/setup-java@v3")
        workflow.contains("java-version: ${jdkVersion}")

        where:
        buildTool               | jdkVersion | workflowName
        BuildTool.GRADLE        | 8          | "gradle.yml"
        BuildTool.GRADLE        | 11         | "gradle.yml"
        BuildTool.GRADLE        | 17         | "gradle.yml"
        BuildTool.GRADLE_KOTLIN | 8          | "gradle.yml"
        BuildTool.GRADLE_KOTLIN | 11         | "gradle.yml"
        BuildTool.GRADLE_KOTLIN | 17         | "gradle.yml"
        BuildTool.MAVEN         | 8          | "maven.yml"
        BuildTool.MAVEN         | 11         | "maven.yml"
        BuildTool.MAVEN         | 17         | "maven.yml"
    }

    @Unroll
    void 'test github-workflow-ci wrapper validation and upload is created for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.DEFAULT_OPTION),
                [GithubCiWorkflowFeature.NAME])
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
