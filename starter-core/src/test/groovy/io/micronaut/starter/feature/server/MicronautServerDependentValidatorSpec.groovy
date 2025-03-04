package io.micronaut.starter.feature.server

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework

class MicronautServerDependentValidatorSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test third part server validation fails with micronaut server features'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .features([Ktor.NAME, 'management', 'tracing-zipkin'])
                .build()

        generate(options)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("ktor cannot be used with these features that depend on a Micronaut Server: [management, tracing-zipkin]")
    }

    void 'test third part server validation fails with some micronaut server features'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .features([Ktor.NAME, 'management', 'tracing-zipkin', 'kafka'])
                .build()
        when:
        generate(options)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("ktor cannot be used with these features that depend on a Micronaut Server: [management, tracing-zipkin]")
    }

    void 'test micronaut server validation passes with micronaut server features'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .features(['netty-server', 'management', 'tracing-zipkin'])
                .build()
        when:
        generate(options)

        then:
        notThrown(Exception)
    }
}
