package io.micronaut.starter.feature.build

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class KotlinSymbolProcessingValidatorSpec  extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test feature ksp is only supported for Kotlin and Gradle'() {
        String featureName = 'ksp'
        when:
        getFeatures([featureName], Language.KOTLIN, TestFramework.JUNIT, BuildTool.MAVEN)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Kotlin Symbol Processing (KSP) is only supported by Gradle")

        when:
        getFeatures([featureName], Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE)

        then:
        noExceptionThrown()
    }
}
