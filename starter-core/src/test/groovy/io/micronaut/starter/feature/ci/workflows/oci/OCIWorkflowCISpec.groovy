package io.micronaut.starter.feature.ci.workflows.oci

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.ci.workflows.gitlab.GitlabCiWorkflowFeature
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Unroll

class OCIWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {

    void "test oracle-cloud-devops-build-ci is a not a preview feature"() {
        given:
        OCICiWorkflowFeature feature = beanContext.getBean(OCICiWorkflowFeature)

        expect:
        !feature.isPreview()
    }

    @Unroll
    void 'test oracle-cloud-devops-build-ci is created for #buildTool and #jdkVersion'(BuildTool buildTool, JdkVersion jdkVersion) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(jdkVersion)
                .features([OCICiWorkflowFeature.NAME])
                .build()

        def output = generate(options)
        def workflow = output["build_spec.yml"]

        then:
        workflow

        switch (jdkVersion.majorVersion()) {
            case 8:
                assert workflow.contains("PKG_NAME=java-1.8.0-openjdk")
                break
            case 11:
                assert workflow.contains("PKG_NAME=java-11-openjdk")
                break
            case 17:
                assert workflow.contains("PKG_NAME=java-17-openjdk")
                break
        }

        if (buildTool.isGradle()) {
            assert workflow.contains("./gradlew build")
            assert workflow.contains("location: build/libs/foo-0.1-all.jar")
            assert workflow.contains("./gradlew dockerBuild")
        } else if (buildTool == BuildTool.MAVEN) {
            assert workflow.contains("./mvnw verify")
            assert workflow.contains("location: target/foo-0.1.jar")
            assert workflow.contains("./mvnw package -Dpackaging=docker")
        }

        workflow.contains("location: foo")

        where:
        [buildTool, jdkVersion] << [BuildTool.values(), MicronautJdkVersionConfiguration.SUPPORTED_JDKS].combinations()
    }

    @Unroll
    void 'test oracle-cloud-devops-build-ci and graalvm is created for #buildTool and #jdkVersion'(BuildTool buildTool, JdkVersion jdkVersion) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(buildTool)
                .javaVersion(JdkVersion.valueOf(jdkVersion.majorVersion()))
                .features([OCICiWorkflowFeature.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .build()
        def output = generate(options)
        def workflow = output["build_spec.yml"]

        then:
        workflow

        workflow.contains("tar -xzf /workspace/graalvm-ce.tar.gz")



        if (buildTool.isGradle()) {
            assert workflow.contains("./gradlew build")
            assert workflow.contains("./gradlew nativeCompile")
            assert workflow.contains("./gradlew dockerBuildNative")
            assert workflow.contains("location: build/libs/foo-0.1-all.jar")
            assert workflow.contains("location: build/native/nativeCompile/foo")
        } else if (buildTool == BuildTool.MAVEN) {
            assert workflow.contains("./mvnw verify")
            assert workflow.contains("./mvnw package -Dpackaging=native-image")
            assert workflow.contains("./mvnw package -Dpackaging=docker-native")
            assert workflow.contains("location: target/foo-0.1.jar")
            assert workflow.contains("location: target/foo")
        }
        if (jdkVersion.majorVersion() == 17) {
            assert workflow.contains("JAVA_HOME=\$(pwd)/graalvm-ce-java17-")
        } else {
            assert workflow.contains("JAVA_HOME=\$(pwd)/graalvm-ce-java11-")
        }
        workflow.contains("JAVA_HOME=\$(pwd)/graalvm-ce")
        workflow.contains("location: foo")

        where:
        [buildTool, jdkVersion] << [BuildTool.values(), MicronautJdkVersionConfiguration.SUPPORTED_JDKS].combinations()
    }
}
