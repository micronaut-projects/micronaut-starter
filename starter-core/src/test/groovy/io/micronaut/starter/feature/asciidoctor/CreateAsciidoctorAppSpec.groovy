package io.micronaut.starter.feature.asciidoctor

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.CommandFixture
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateAsciidoctorAppSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle create-app for asciidoctor feature, language=#language'() {
        given:
        generateDefaultProject(language, BuildTool.GRADLE, ['asciidoctor'])

        when:
        executeGradleCommand('asciidoctor')

        then:
        testOutputContains('BUILD SUCCESSFUL')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven create-app for asciidoctor feature, language=#language'() {
        given:
        generateDefaultProject(language, BuildTool.MAVEN, ['asciidoctor'])

        when:
        executeMavenCommand('generate-resources')

        then:
        testOutputContains('BUILD SUCCESS')

        where:
        language << Language.values()
    }

}
