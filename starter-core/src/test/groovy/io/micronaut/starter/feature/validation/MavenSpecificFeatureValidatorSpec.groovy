package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class MavenSpecificFeatureValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test feature #featureName is only supported for Maven'(String featureName) {
        when:
        getFeatures([featureName], Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature only supported by Maven")

        when:
        getFeatures([featureName], Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN)

        then:
        noExceptionThrown()

        where:
        featureName << ['spring-boot-maven-plugin', 'groovy-maven-plus-plugin']
    }

}
