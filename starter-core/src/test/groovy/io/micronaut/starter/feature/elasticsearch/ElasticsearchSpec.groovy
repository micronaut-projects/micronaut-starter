package io.micronaut.starter.feature.elasticsearch

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class ElasticsearchSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle elasticsearch feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['elasticsearch'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.configuration:micronaut-elasticsearch"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
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
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }

    void 'test elasticsearch configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['elasticsearch'])

        then:
        commandContext.configuration.get('elasticsearch.httpHosts'.toString()) == '"http://localhost:9200,http://127.0.0.2:9200"'
    }
}
