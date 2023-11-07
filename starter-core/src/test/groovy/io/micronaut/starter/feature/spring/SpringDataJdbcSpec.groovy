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

class SpringDataJdbcSpec extends ApplicationContextSpec {

    @Shared
    @Subject
    SpringDataJdbc springDataJdbc = beanContext.getBean(SpringDataJdbc)

    void 'spring-data-jdbc belongs to Spring category'() {
        expect:
        Category.SPRING == springDataJdbc.category
    }

    void 'spring-data-jdbc is visible'() {
        expect:
        springDataJdbc.visible
    }

    void 'spring-data-jdbc title and description are different'() {
        expect:
        springDataJdbc.getTitle()
        springDataJdbc.getDescription()
        springDataJdbc.getTitle() != springDataJdbc.getDescription()
    }

    @Unroll
    void 'feature spring-data-jdbc supports every type of application type. applicationType=#applicationType'() {
        expect:
        springDataJdbc.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'test spring-data-jdbc features'() {
        when:
        Features features = getFeatures(['spring-data-jdbc'])

        then:
        features.contains('data-jdbc')
        features.contains('spring')
    }

    @Unroll
    void 'test spring-data-jdbc with Gradle for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['spring-data-jdbc', 'kapt'])
                .language(language)
                .render()

        then:
        template.contains("${getGradleAnnotationProcessorScope(language)}(\"io.micronaut.spring:micronaut-spring-annotation\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-spring")')
        template.contains('implementation("org.springframework:spring-jdbc")')

        where:
        language << Language.values().toList()
    }

    void 'test maven spring-data-jdbc feature for java'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-data-jdbc'])
                .language(Language.JAVA)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
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
    }

    void 'test maven spring-data-jdbc feature for groovy'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-data-jdbc'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.spring</groupId>
      <artifactId>micronaut-spring-annotation</artifactId>
      <scope>provided</scope>
    </dependency>
""")
    }

    void 'test maven spring-data-jdbc feature for kotlin'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['spring-data-jdbc'])
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-spring</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
               <annotationProcessorPath>
                 <groupId>io.micronaut.spring</groupId>
                 <artifactId>micronaut-spring-annotation</artifactId>
                 <version>\${micronaut.spring.version}</version>
               </annotationProcessorPath>
""")
    }
}
