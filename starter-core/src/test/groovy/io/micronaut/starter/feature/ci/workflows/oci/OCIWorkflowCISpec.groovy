package io.micronaut.starter.feature.ci.workflows.oci

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class OCIWorkflowCISpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test oci-devops-build-ci is created for #buildTool and #jdkVersion'(BuildTool buildTool, JdkVersion jdkVersion) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.valueOf(jdkVersion.majorVersion())),
                [OCICiWorkflowFeature.NAME])
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
        } else if (buildTool == BuildTool.MAVEN) {
            assert workflow.contains("./mvnw verify")
            assert workflow.contains("location: target/foo-0.1.jar")
        }

        where:
        [buildTool, jdkVersion] << [BuildTool.values(), JdkVersion.values()].combinations()
    }
}
