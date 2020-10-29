package io.micronaut.starter.feature.spring

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringWebSpec extends BeanContextSpec {

    @Shared
    @Subject
    SpringWeb springWeb = beanContext.getBean(SpringWeb)

    void 'spring-web belongs to Spring category'() {
        expect:
        Category.SPRING == springWeb.category
    }

    void 'spring-web is visible'() {
        expect:
        springWeb.visible
    }

    void 'spring-web title and description are different'() {
        expect:
        springWeb.getTitle()
        springWeb.getDescription()
        springWeb.getTitle() != springWeb.getDescription()
    }

    @Unroll
    void 'feature spring-web does not support type: #applicationType'(ApplicationType applicationType) {
        expect:
        !springWeb.supports(applicationType)

        where:
        applicationType << (ApplicationType.values().toList() - ApplicationType.DEFAULT)
    }

    @Unroll
    void 'feature spring-web supports #applicationType'(ApplicationType applicationType) {
        expect:
        springWeb.supports(applicationType)

        where:
        applicationType << [ApplicationType.DEFAULT]
    }

    void 'test spring-web features'() {
        when:
        Features features = getFeatures(['spring-web'])

        then:
        features.contains('spring')
    }

    @Unroll
    void 'test spring-web with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-web'], language), false).render().toString()

        then:
        template.contains("$scope(\"io.micronaut.spring:micronaut-spring-web-annotation\")")
        template.contains('implementation("org.springframework.boot:spring-boot-starter-web")')
        template.contains('implementation("io.micronaut:micronaut-http-server")')
        template.contains('runtime("io.micronaut.spring:micronaut-spring-web")')

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven spring-web feature'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-web'], Language.JAVA), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-server</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-web</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.spring</groupId>
              <artifactId>micronaut-spring-web-annotation</artifactId>
              <version>\${micronaut.spring.version}</version>
            </path>
""")
        template.contains("""
                <path>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-web-annotation</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </path>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-web'], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-server</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-web</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.count("""
                <annotationProcessorPath>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-web-annotation</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </annotationProcessorPath>
""") == 2

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-web'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-server</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-web</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-web-annotation</artifactId>
      <scope>compile</scope>
    </dependency>
""")

    }
}
