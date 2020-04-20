package io.micronaut.starter.generator

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Unroll

class CreateAppSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test basic create-app for lang=#lang'() {
        given:
        generateDefaultProject(lang)

        when:
        executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

    @Unroll
    void 'test basic create-app for lang=#lang and maven'() {
        given:
        generateDefaultProject(lang, BuildTool.MAVEN)

        when:
        executeMavenCommand("mn:run")

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

}
