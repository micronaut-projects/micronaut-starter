package io.micronaut.starter.feature.graphql

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GraphQLSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature graphql contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['graphql'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-graphql/latest/guide/index.html")
    }

    @Shared
    @Subject
    GraphQL graphQl = beanContext.getBean(GraphQL)

    void "graphQl belongs to API category"() {
        expect:
        Category.API == graphQl.category
    }

    @Unroll
    void 'test gradle graphql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['graphql'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.graphql:micronaut-graphql")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven graphql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['graphql'])
                .render()

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

}
