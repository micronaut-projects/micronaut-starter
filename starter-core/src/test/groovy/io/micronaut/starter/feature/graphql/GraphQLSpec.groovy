package io.micronaut.starter.feature.graphql

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class GraphQLSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle graphql feature for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['graphql'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.graphql:micronaut-graphql")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven graphql feature for language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['graphql'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.graphql</groupId>
      <artifactId>micronaut-graphql</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    void 'test graphql configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['graphql'])

        then:
        commandContext.configuration.get('#graphql.graphiql.enabled'.toString()) == true
    }
}
