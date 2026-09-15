package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class GradleSpecificFeatureValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test feature ksp is only supported for Kotlin and Gradle'() {
        given:
        String featureName = 'ksp'
        BuildTool buildTool = BuildTool.MAVEN

        when:
        getFeatures([featureName], Language.KOTLIN, TestFramework.JUNIT, buildTool)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature ${featureName} does not support build tool ${buildTool}. ")

        when:
        getFeatures([featureName], Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE)

        then:
        noExceptionThrown()

        when:
        getFeatures([featureName], Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE_KOTLIN)

        then:
        noExceptionThrown()
    }
}
