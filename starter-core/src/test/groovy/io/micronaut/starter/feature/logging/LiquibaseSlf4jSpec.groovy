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

class LiquibaseSlf4jSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    LiquibaseSlf4j feature = beanContext.getBean(LiquibaseSlf4j)

    void "test value of third party documentation"() {
        feature.getThirdPartyDocumentation() == 'https://github.com/mattbertolini/liquibase-slf4j'
    }

    void "liquibase-slf4j supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
        description = applicationType.name
    }

    void "liquibase-slf4j is logging category"() {
        expect:
        feature.category == Category.LOGGING
    }

    void "test dependency added for liquibase-slf4j feature for build tool #buildTool"(BuildTool buildTool) {

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["liquibase-slf4j", "liquibase"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("com.mattbertolini", "liquibase-slf4j", Scope.RUNTIME)
        verifier.hasDependency("io.micronaut.liquibase", "micronaut-liquibase")
        !verifier.hasDependency("org.slf4j", "jul-to-slf4j")

        where:
        buildTool << BuildTool.values()
    }
}
