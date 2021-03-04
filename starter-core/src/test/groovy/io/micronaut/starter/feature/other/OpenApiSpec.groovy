package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class OpenApiSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature openapi contains links to micronaut docs'() {
        when:
        def output = generate(['openapi'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.openapis.org")
        readme.contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html")
    }

    void "openApi belongs to API category"() {
        expect:
        Category.API == beanContext.getBean(OpenApi).category
    }

    @Unroll
    void 'test swagger with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['openapi'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.swagger.core.v3:swagger-annotations")')
        template.contains("$scope(\"io.micronaut.openapi:micronaut-openapi\")")

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven swagger feature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['openapi'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("<micronaut.openapi.version>")
        template.contains("</micronaut.openapi.version>")
        template.contains('''
            <path>
              <groupId>io.micronaut.openapi</groupId>
              <artifactId>micronaut-openapi</artifactId>
              <version>${micronaut.openapi.version}</version>
            </path>
''')

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['openapi'])
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''
                <annotationProcessorPath>
                  <groupId>io.micronaut.openapi</groupId>
                  <artifactId>micronaut-openapi</artifactId>
                  <version>${micronaut.openapi.version}</version>
                </annotationProcessorPath>
''')

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['openapi'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.openapi</groupId>
      <artifactId>micronaut-openapi</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
