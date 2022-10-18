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

class LocalStackSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    LocalStack localStack = beanContext.getBean(LocalStack)

    void 'test readme.md with feature localstack contains links to 3rd party docs'() {
        when:
        def output = generate(['localstack'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.testcontainers.org/modules/localstack/")
    }

    void "test localstack belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == localStack.category
    }

    @Unroll
    void 'test gradle localstack feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['localstack'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:testcontainers")')
        template.contains('testImplementation("org.testcontainers:localstack")')
        template.contains('testImplementation("com.amazonaws:aws-java-sdk-core")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven localstack feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['localstack'])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>localstack</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-java-sdk-core</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

}
