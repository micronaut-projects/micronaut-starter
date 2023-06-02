package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class MicronautServerDependentValidatorSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test third part server validation fails with micronaut server features'() {
        when:
        Options options = new Options(Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE)
        generate(ApplicationType.DEFAULT, options, [Ktor.NAME, 'management', 'tracing-zipkin'])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("ktor cannot be used with these features that depend on a Micronaut Server: [management, tracing-zipkin]")
    }

    void 'test third part server validation fails with some micronaut server features'() {
        when:
        Options options = new Options(Language.KOTLIN, TestFramework.JUNIT, BuildTool.GRADLE)
        generate(ApplicationType.DEFAULT, options, [Ktor.NAME, 'management', 'tracing-zipkin', 'kafka'])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("ktor cannot be used with these features that depend on a Micronaut Server: [management, tracing-zipkin]")
    }

    void 'test micronaut server validation passes with micronaut server features'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE)
        generate(ApplicationType.DEFAULT, options, ['netty-server', 'management', 'tracing-zipkin'])

        then:
        notThrown(Exception)
    }
}
