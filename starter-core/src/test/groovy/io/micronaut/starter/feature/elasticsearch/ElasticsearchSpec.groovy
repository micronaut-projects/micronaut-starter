package io.micronaut.starter.feature.elasticsearch

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ElasticsearchSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void 'test gradle elasticsearch feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['elasticsearch'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-elasticsearch"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven elasticsearch feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['elasticsearch'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-elasticsearch</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test elasticsearch configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['elasticsearch'])

        then:
        commandContext.configuration.get('elasticsearch.httpHosts'.toString()) == '"http://localhost:9200,http://127.0.0.2:9200"'
    }
}
