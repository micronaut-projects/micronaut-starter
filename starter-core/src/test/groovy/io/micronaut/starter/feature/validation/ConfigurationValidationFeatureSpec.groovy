package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.PendingFeature

class ConfigurationValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @PendingFeature
    void 'configuration validation block is not added'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                [])
        String pom = output['pom.xml']

        then:
        pom

        and: 'configuration validation is not in pom, it is in parent pom'
        !pom.contains("<configurationValidation>")
        !pom.contains("<validateDependencyInjection>true</validateDependencyInjection>")
        !pom.contains("<failOnNotPresent>true</failOnNotPresent>")
        !pom.contains("<cacheEnabled>true</cacheEnabled>")
        !pom.contains("</configurationValidation>")
    }
}
