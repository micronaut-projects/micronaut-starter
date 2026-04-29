package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class MicronautProcessingCompilerArgsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    MicronautProcessingCompilerArgs feature = beanContext.getBean(MicronautProcessingCompilerArgs)

    void "micronaut-processing-compiler-args is not visible"() {
        expect:
        !feature.visible
    }

    void 'test maven compiler args'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN),
                [])
        def maven = output['pom.xml']

        then:
        maven
        maven.contains("<arg>-Amicronaut.processing.group=example.micronaut</arg>")
        maven.contains("<arg>-Amicronaut.processing.module=foo</arg>")
    }
}
