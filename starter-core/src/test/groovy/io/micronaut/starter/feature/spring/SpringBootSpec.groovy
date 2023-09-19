package io.micronaut.starter.feature.spring

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class SpringBootSpec extends ApplicationContextSpec {

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
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['spring-boot', 'kapt'])
                .language(language)
                .render()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.spring:micronaut-spring-boot-annotation\")")
        template.contains('implementation("org.springframework.boot:spring-boot-starter-web")')
        template.contains('runtimeOnly("io.micronaut.spring:micronaut-spring-boot")')

        where:
        language << Language.values().toList()
    }

    void 'test maven spring-boot feature'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-boot'])
                .render()

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
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.spring</groupId>
              <artifactId>micronaut-spring-boot-annotation</artifactId>
              <version>\${micronaut.spring.version}</version>
            </path>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.spring</groupId>
              <artifactId>micronaut-spring-annotation</artifactId>
              <version>\${micronaut.spring.version}</version>
              <exclusions>
                <exclusion>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-core</artifactId>
                </exclusion>
              </exclusions>
            </path>
""")
        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-boot'])
                .language(Language.KOTLIN)
                .render()

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
      <scope>runtime</scope>
    </dependency>
""")
        template.count('''\
               <annotationProcessorPath>
                 <groupId>io.micronaut.spring</groupId>
                 <artifactId>micronaut-spring-boot-annotation</artifactId>
                 <version>${micronaut.spring.version}</version>
               </annotationProcessorPath>
''') == 2

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-boot'])
                .language(Language.GROOVY)
                .render()

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
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['spring-boot'])
                .language(language)
                .render()

        then:
        template.count('implementation("org.springframework.boot:spring-boot-starter-web")') == 1

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-boot'])
                .language(language)
                .render()

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
