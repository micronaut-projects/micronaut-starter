package io.micronaut.starter.feature.ci.workflows.gcp

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.ci.workflows.aws.AWSCiWorkflowFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Unroll

class GoogleCloudWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test gcp-workflow-ci wrapper validation and upload is created for Gradle'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([GoogleCloudCiWorkflowFeature.NAME])
                .build()

        def output = generate(options)
        def workflow = output["cloudbuild.yaml"]

        then:
        workflow
        workflow.contains("entrypoint: gradle")
        workflow.contains("args: [\"build\"]")
    }

    @Unroll
    void 'test gcp-workflow-ci wrapper validation and upload is created for Maven'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([GoogleCloudCiWorkflowFeature.NAME])
                .build()

        def output = generate(options)
        def workflow = output["cloudbuild.yaml"]

        then:
        workflow
        workflow.contains("entrypoint: mvn")
        workflow.contains("args: [\"package\"]")
    }

}
