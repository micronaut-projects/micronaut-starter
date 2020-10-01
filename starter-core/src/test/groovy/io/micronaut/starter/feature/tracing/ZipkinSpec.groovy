package io.micronaut.starter.feature.tracing

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ZipkinSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature tracing-zipkin contains links to micronaut docs'() {
        when:
        def output = generate(['tracing-zipkin'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#zipkin")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#zipkin")
    }

    @Unroll
    void 'test gradle tracing-zipkin feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['tracing-zipkin'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-tracing")')
        template.contains('implementation("io.opentracing.brave:brave-opentracing")')
        template.contains('runtimeOnly("io.zipkin.brave:brave-instrumentation-http")')
        template.contains('runtimeOnly("io.zipkin.reporter2:zipkin-reporter")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven tracing-zipkin feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['tracing-zipkin'], language), []).render().toString()

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
      <groupId>io.opentracing.brave</groupId>
      <artifactId>brave-opentracing</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.zipkin.brave</groupId>
      <artifactId>brave-instrumentation-http</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.zipkin.reporter2</groupId>
      <artifactId>zipkin-reporter</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test tracing-zipkin configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['tracing-zipkin'])

        then:
        commandContext.configuration.get('tracing.zipkin.enabled'.toString()) == true
        commandContext.configuration.get('tracing.zipkin.http.url'.toString()) == 'http://localhost:9411'
        commandContext.configuration.get('tracing.zipkin.sampler.probability'.toString()) == 0.1
    }

}
