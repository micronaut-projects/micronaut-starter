package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MicronautRestAssuredSpec extends ApplicationContextSpec implements CommandOutputFixture {

    final String FEATURE = "micronaut-test-rest-assured"

    @Shared
    @Subject
    MicronautRestAssured micronautRestAssured = beanContext.getBean(MicronautRestAssured)

    void 'test readme.md with feature micronaut-test-rest-assured contains links to docs'() {
        when:
        Map<String, String> output = generate([FEATURE])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://rest-assured.io/#docs")
        readme.contains("https://micronaut-projects.github.io/micronaut-test/latest/guide/#restAssured")
    }

    void "test micronaut-test-rest-assured belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == micronautRestAssured.category
    }

    @Unroll
    void 'test gradle micronaut-test-rest-assured feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([FEATURE])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("io.micronaut.test:micronaut-test-rest-assured")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven micronaut-test-rest-assured feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([FEATURE])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.test</groupId>
      <artifactId>micronaut-test-rest-assured</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}