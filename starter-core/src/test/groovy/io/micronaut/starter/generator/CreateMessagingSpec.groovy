package io.micronaut.starter.generator

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.messaging.Platform
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll

class CreateMessagingSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-messaging-app for lang=#lang and platform=#platform'() {
        given:
        generateMessagingProject(lang, BuildTool.GRADLE, platform)

        when:
        executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        where:
        [lang, platform] << [[Language.JAVA, Language.GROOVY, Language.KOTLIN, null], [Platform.KAFKA, Platform.RABBITMQ]].combinations()
    }

    @Unroll
    void 'test basic create-messaging-app for lang=#lang and platform=#platform and maven'() {
        given:
        generateMessagingProject(lang, BuildTool.MAVEN, platform)

        when:
        executeMavenCommand("mn:run")

        then:
        testOutputContains("Startup completed")

        where:
        [lang, platform] << [[Language.JAVA, Language.GROOVY, Language.KOTLIN, null], [Platform.KAFKA, Platform.RABBITMQ]].combinations()
    }

}
