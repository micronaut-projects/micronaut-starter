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

class SpringBootSpec extends BeanContextSpec {

    @Shared
    @Subject
    SpringBoot springBoot = beanContext.getBean(SpringBoot)

    void 'spring-boot belongs to Spring category'() {
        expect:
        Category.SPRING == springBoot.category
    }

    void 'spring-boot is visible'() {
        expect:
        springBoot.visible
    }

    void 'spring-boot title and description are different'() {
        expect:
        springBoot.getTitle()
        springBoot.getDescription()
        springBoot.getTitle() != springBoot.getDescription()
    }

    @Unroll
    void 'feature spring-boot supports every type of application type. applicationType=#applicationType'() {
        expect:
        springBoot.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'test spring-boot features'() {
        when:
        Features features = getFeatures(['spring-boot'])

        then:
        features.contains('spring')
    }

    @Unroll
    void 'test spring-boot with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], language), false).render().toString()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.spring:micronaut-spring-boot\")")
        template.contains('implementation("org.springframework.boot:spring-boot-starter-web")')
        template.contains('runtime("io.micronaut.spring:micronaut-spring-boot")')

        where:
        language << Language.values().toList()
    }

    void 'test maven spring-boot feature'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], Language.JAVA), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-boot</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.spring</groupId>
              <artifactId>micronaut-spring-boot</artifactId>
              <version>\${micronaut.spring.version}</version>
            </path>
""")
        template.contains("""
                <path>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-boot</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </path>
""")

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-boot</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.count("""
                <annotationProcessorPath>
                  <groupId>io.micronaut.spring</groupId>
                  <artifactId>micronaut-spring-boot</artifactId>
                  <version>\${micronaut.spring.version}</version>
                </annotationProcessorPath>
""") == 2

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-boot</artifactId>
      <scope>compile</scope>
    </dependency>
""")

    }

    void 'test spring-web and spring-boot only add spring-boot-starter-web dependency once'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], language), false).render().toString()

        then:
        template.count('implementation("org.springframework.boot:spring-boot-starter-web")') == 1

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-boot'], language), []).render().toString()

        then:
        template.count("""
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>
    </dependency>
""") == 1

        where:
        language << Language.values().toList()
    }
}
