package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class Slf4jSimpleLoggerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Slf4jSimpleLogger slf4jSimpleLogger = beanContext.getBean(Slf4jSimpleLogger)

    void "test value of third party documentation"() {
        slf4jSimpleLogger.getThirdPartyDocumentation() == 'https://github.com/GoodforGod/slf4j-simple-logger'
    }

    void "Slf4jSimpleLogger supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        slf4jSimpleLogger.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "Slf4jSimpleLogger is logging category"() {
        expect:
        slf4jSimpleLogger.category == Category.LOGGING
    }

    void "test dependency added for Slf4jSimpleLogger feature for build tool #buildTool"(BuildTool buildTool) {

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Slf4jSimpleLogger.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.goodforgod", "slf4j-simple-logger", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }
}
