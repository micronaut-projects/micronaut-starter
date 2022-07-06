package io.micronaut.starter.feature.ci.workflows.gcp

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class GoogleCloudWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test gcp-workflow-ci wrapper validation and upload is created for Gradle'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.DEFAULT_OPTION),
                [GoogleCloudCiWorkflowFeature.NAME])
        def workflow = output["cloudbuild.yaml"]

        then:
        workflow
        workflow.contains("entrypoint: gradle")
        workflow.contains("args: [\"build\"]")
    }

    @Unroll
    void 'test gcp-workflow-ci wrapper validation and upload is created for Maven'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.DEFAULT_OPTION),
                [GoogleCloudCiWorkflowFeature.NAME])
        def workflow = output["cloudbuild.yaml"]

        then:
        workflow
        workflow.contains("entrypoint: mvn")
        workflow.contains("args: [\"package\"]")
    }

}
