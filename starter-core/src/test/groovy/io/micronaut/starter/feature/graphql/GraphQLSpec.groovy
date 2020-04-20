package io.micronaut.starter.feature.graphql

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class GraphQLSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle graphql feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['graphql'], language)).render().toString()

        then:
        template.contains('implementation "io.micronaut.graphql:micronaut-graphql"')

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    @Unroll
    void 'test maven graphql feature for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['graphql'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.graphql</groupId>
      <artifactId>micronaut-graphql</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << [Language.java, Language.kotlin, Language.groovy]
    }

    void 'test graphql configuration'() {
        when:
        GeneratorContext commandContext = buildCommandContext(['graphql'])

        then:
        commandContext.configuration.get('#graphql.graphiql.enabled'.toString()) == true
    }
}
