package io.micronaut.starter.feature.spring

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringSpec extends BeanContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    Spring spring = beanContext.getBean(Spring)

    void 'test readme.md with feature spring contains links to micronaut docs'() {
        when:
        def output = generate(['spring'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-spring/latest/guide/index.html")
    }


    void 'spring belongs to Spring category'() {
        expect:
        Category.SPRING == spring.category
    }

    void 'spring is visible'() {
        expect:
        spring.visible
    }

    void 'spring title and description are different'() {
        expect:
        spring.getTitle()
        spring.getDescription()
        spring.getTitle() != spring.getDescription()
    }

    @Unroll
    void 'feature spring supports every type of application type. applicationType=#applicationType'() {
        expect:
        spring.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    @Unroll
    void 'test spring with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring'], language), false).render().toString()

        then:
        template.contains("$scope(\"io.micronaut.spring:micronaut-spring-annotation\")")
        template.contains('implementation("org.springframework.boot:spring-boot-starter")')

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven spring feature'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring'], Language.JAVA), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.spring</groupId>
              <artifactId>micronaut-spring-annotation</artifactId>
              <version>\${micronaut.spring.version}</version>
            </path>
""")
        template.contains("""
                <path>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-annotation</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </path>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring'], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.count("""
                <annotationProcessorPath>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-annotation</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </annotationProcessorPath>
""") == 2

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-annotation</artifactId>
      <scope>compile</scope>
    </dependency>
""")

    }
}
