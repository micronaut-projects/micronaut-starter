package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class ProjectLombokSpec extends BeanContextSpec implements CommandOutputFixture {
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

    void "openApi belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == projectLombok.category
    }

    @Unroll
    void 'test lombok with Gradle for Java'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['lombok'], Language.JAVA), false, [], []).render().toString()

        then:
        template.contains('annotationProcessor("org.projectlombok:lombok")')
        template.contains('compileOnly("org.projectlombok:lombok")')
    }

    @Unroll
    void 'test lombok with Gradle for only Java'() {
        when:
        buildGradle.template(ApplicationType.DEFAULT, buildProject(),
                getFeatures(['lombok'], language), false, [], []).render().toString()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("The selected features are incompatible.")

        where:
        language << [Language.KOTLIN , Language.GROOVY]
    }

    void 'test maven lombok feature'() {
        when:
        def context = buildGeneratorContext(
                ['lombok'],
                new Options(Language.JAVA, TestFramework.SPOCK, BuildTool.MAVEN),
                ApplicationType.DEFAULT)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(),
                context.getFeatures(), context.getBuildProperties().getProperties(), [], []).render().toString()

        then:
        // ensure we use version from Micronaut BOM
        !template.contains("""
    <lombok.version>1.18.16</lombok.version>
""")
        // make sure lombok is before inject-java or it won't work
        template.contains("""
          <annotationProcessorPaths combine.self="override">
            <path>
              <!-- must precede micronaut-inject-java -->
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>\${lombok.version}</version>
            </path>
""")
        template.contains("""
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <scope>provided</scope>
    </dependency>
""")
    }
}
