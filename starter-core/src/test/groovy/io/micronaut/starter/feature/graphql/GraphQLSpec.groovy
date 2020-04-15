package io.micronaut.starter.feature.graphql

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

class GraphQLSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

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
        CommandContext commandContext = buildCommandContext(['graphql'])

        then:
        commandContext.configuration.get('#graphql.graphiql.enabled'.toString()) == true
    }
}
