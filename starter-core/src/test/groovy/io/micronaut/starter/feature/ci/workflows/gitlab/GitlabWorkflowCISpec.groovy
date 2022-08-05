package io.micronaut.starter.feature.ci.workflows.gitlab

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.graalvm.GraalVM
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
        workflow.contains("image: eclipse-temurin:${jdkVersion}")
        workflow.contains("script: ./mvnw \$MAVEN_CLI_OPTS compile")
        workflow.contains("script: ./mvnw \$MAVEN_CLI_OPTS verify")

        where:
        jdkVersion << [8, 11, 17]
    }

    @Unroll
    void 'test gitlab-workflow-ci is created for Gradle and #jdkVersion'(int jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.valueOf(jdkVersion)),
                [GitlabCiWorkflowFeature.NAME])
        def workflow = output[".gitlab-ci.yml"]

        then:
        workflow
        workflow.contains("image: eclipse-temurin:${jdkVersion}")
        workflow.contains("script: ./gradlew --build-cache assemble")
        workflow.contains("script: ./gradlew check")

        where:
        jdkVersion << [8, 11, 17]
    }

    @Unroll
    void 'test gitlab-workflow-ci is created for Maven, GraalVM and #jdkVersion'(int jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.valueOf(jdkVersion)),
                [GitlabCiWorkflowFeature.NAME, GraalVM.FEATURE_NAME_GRAALVM])
        def workflow = output[".gitlab-ci.yml"]

        then:
        workflow
        workflow.contains("image: ghcr.io/graalvm/graalvm-ce:ol8-java${jdkVersion}")
        workflow.contains("script: ./mvnw \$MAVEN_CLI_OPTS compile")
        workflow.contains("script: ./mvnw \$MAVEN_CLI_OPTS verify")
        workflow.contains("script: ./mvnw \$MAVEN_CLI_OPTS package -Dpackaging=native-image")

        where:
        jdkVersion << [11, 17]
    }

    @Unroll
    void 'test gitlab-workflow-ci is created for Gradle, GraalVM and #jdkVersion'(int jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.valueOf(jdkVersion)),
                [GitlabCiWorkflowFeature.NAME, GraalVM.FEATURE_NAME_GRAALVM])
        def workflow = output[".gitlab-ci.yml"]

        then:
        workflow
        workflow.contains("image: ghcr.io/graalvm/graalvm-ce:ol8-java${jdkVersion}")
        workflow.contains("script: ./gradlew --build-cache assemble")
        workflow.contains("script: ./gradlew check")
        workflow.contains("script: ./gradlew nativeCompile")

        where:
        jdkVersion << [11, 17]
    }
}
