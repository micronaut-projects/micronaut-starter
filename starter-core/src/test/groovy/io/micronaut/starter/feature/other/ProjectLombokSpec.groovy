package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class ProjectLombokSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    ProjectLombok projectLombok = beanContext.getBean(ProjectLombok)

    void 'test readme.md with feature projectLombok contains links to micronaut docs'() {
        when:
        def output = generate(['lombok'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://projectlombok.org/features/all")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#lombok")
    }

    void "lombok belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == projectLombok.category
    }

    @Unroll
    void 'test lombok with Gradle for Java'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['lombok'])
                .render()

        then:
        template.contains('annotationProcessor("org.projectlombok:lombok")')
        template.contains('compileOnly("org.projectlombok:lombok")')
    }

    @Unroll
    void 'test lombok with Gradle for only Java'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['lombok'])
                .language(language)
                .render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("The selected features are incompatible.")

        where:
        language << [Language.KOTLIN , Language.GROOVY]
    }

    void 'test maven lombok feature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['lombok'])
                .render()

        then:
        // ensure we use version from Micronaut BOM
//        !template.contains("""
//    <lombok.version>1.18.16</lombok.version>
//""")
        // make sure lombok is before inject-java or it won't work
        template.contains('''\
          <annotationProcessorPaths combine.self="override">
            <path>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>${lombok.version}</version>
            </path>
''')
        template.contains("""
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <scope>provided</scope>
    </dependency>
""")
    }
}
