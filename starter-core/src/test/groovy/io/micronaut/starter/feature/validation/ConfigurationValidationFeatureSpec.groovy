package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class ConfigurationValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {
    void 'by default configuration validation block is added'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                [])
        String pom = output['pom.xml']

        then:
        pom
        pom.contains("<configurationValidation>")
        pom.contains("<validateDependencyInjection>true</validateDependencyInjection>")
        pom.contains("<failOnNotPresent>true</failOnNotPresent>")
        pom.contains("<cacheEnabled>true</cacheEnabled>")
        pom.contains("<enabled>true</enabled>")
        pom.contains("</configurationValidation>")
    }
}
