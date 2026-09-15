package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class MavenSpecificFeatureValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test feature #featureName is only supported for Maven'(String featureName) {
        given:
        BuildTool buildTool = BuildTool.GRADLE
        when:
        getFeatures([featureName], Language.JAVA, TestFramework.JUNIT, buildTool)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature ${featureName} does not support build tool ${buildTool}. ")

        when:
        getFeatures([featureName], Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN)

        then:
        noExceptionThrown()

        where:
        featureName << ['spring-boot-maven-plugin', 'groovy-maven-plus-plugin']
    }

}
