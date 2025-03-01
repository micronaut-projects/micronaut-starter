package io.micronaut.starter.feature.ci.workflows.aws

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
import spock.lang.Unroll

class AWSWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test aws-workflow-ci wrapper validation and upload is created for Gradle'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([AWSCiWorkflowFeature.NAME])
                .build()
        def output = generate(options)
        def workflow = output["buildspec.yml"]

        then:
        workflow
        workflow.contains("- ./gradlew build")
        workflow.contains("- 'foo-0.1-all.jar'")
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
                .features([AWSCiWorkflowFeature.NAME])
                .build()

        def output = generate(options)
        def workflow = output["buildspec.yml"]

        then:
        workflow
        workflow.contains("- mvn")
        workflow.contains("- 'foo-0.1.jar'")
    }
}