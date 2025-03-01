package io.micronaut.starter.feature.ci.workflows.gitlab

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.ci.workflows.github.GithubCiWorkflowFeature
import io.micronaut.starter.feature.ci.workflows.oci.OCICiWorkflowFeature
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Unroll

class GitlabWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {
    @Unroll
    void 'test gitlab-workflow-ci is created for Maven and #jdkVersion'(int jdkVersion) {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(JdkVersion.valueOf(jdkVersion))
                .features([GitlabCiWorkflowFeature.NAME])
                .build()
        when:
        def output = generate(options)
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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(JdkVersion.valueOf(jdkVersion))
                .features([GitlabCiWorkflowFeature.NAME])
                .build()
        def output = generate(options)
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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(JdkVersion.valueOf(jdkVersion))
                .features([GitlabCiWorkflowFeature.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .build()

        def output = generate(options)
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
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(JdkVersion.valueOf(jdkVersion))
                .features([GitlabCiWorkflowFeature.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .build()
        def output = generate(options)
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
