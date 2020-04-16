package io.micronaut.starter.feature.tracing

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class JaegerSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle tracing-jaeger feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['tracing-jaeger'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut:micronaut-tracing"')
        template.contains('runtimeOnly "io.jaegertracing:jaeger-thrift"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven tracing-jaeger feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['tracing-jaeger'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-tracing</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.jaegertracing</groupId>
      <artifactId>jaeger-thrift</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test tracing-jaeger configuration'() {
        when:
        CommandContext commandContext = buildCommandContext(['tracing-jaeger'])

        then:
        commandContext.configuration.get('tracing.jaeger.enabled'.toString()) == true
        commandContext.configuration.get('tracing.jaeger.sampler.probability'.toString()) == 0.1
    }

}
