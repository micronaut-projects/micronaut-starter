package io.micronaut.starter.feature.tracing

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class JaegerSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature tracing-jaeger contains links to micronaut docs'() {
        when:
        def output = generate(['tracing-jaeger'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#jaeger")
        readme.contains("https://www.jaegertracing.io")
    }

    @Unroll
    void 'test gradle tracing-jaeger feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['tracing-jaeger'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-tracing")')
        template.contains('runtimeOnly("io.jaegertracing:jaeger-thrift")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven tracing-jaeger feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['tracing-jaeger'], language), []).render().toString()

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
        language << Language.values().toList()
    }

    void 'test tracing-jaeger configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['tracing-jaeger'])

        then:
        commandContext.configuration.get('tracing.jaeger.enabled'.toString()) == true
        commandContext.configuration.get('tracing.jaeger.sampler.probability'.toString()) == 0.1
    }

}
