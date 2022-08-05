package io.micronaut.starter.feature.ci.workflows.gitlab

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Unroll

class GitlabWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test gitlab-workflow-ci is created for Maven and #jdkVersion'(int jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.valueOf(jdkVersion)),
                [GitlabCiWorkflowFeature.NAME])
        def workflow = output[".gitlab-ci.yml"]

        then:
        workflow
        workflow.contains("image: maven:3-eclipse-temurin-${jdkVersion}")
        workflow.contains("script: mvn \$MAVEN_CLI_OPTS compile")
        workflow.contains("script: mvn \$MAVEN_CLI_OPTS verify")

        where:
        jdkVersion << [8, 11, 17]
    }

    @Unroll
    void 'test gitlab-workflow-ci is created for Gradle'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.DEFAULT_OPTION),
                [GitlabCiWorkflowFeature.NAME])
        def workflow = output[".gitlab-ci.yml"]

        then:
        workflow
        workflow.contains("image: gradle:alpine")
        workflow.contains("script: gradle --build-cache assemble")
        workflow.contains("script: gradle check")
    }
}
