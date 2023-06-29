package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class HibernateGormSpec extends ApplicationContextSpec {

    void "test hibernate gorm features"() {
        when:
        Features features = getFeatures(['hibernate-gorm'])

        then:
        features.contains("groovy")
        features.contains("h2")
        features.contains("hibernate-validator")
        features.contains("hibernate-gorm")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["hibernate-gorm"])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-hibernate-gorm")')
        template.contains('implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
        template.contains("runtimeOnly(\"org.apache.tomcat:tomcat-jdbc\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['hibernate-gorm'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.groovy</groupId>
      <artifactId>micronaut-hibernate-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.beanvalidation</groupId>
      <artifactId>micronaut-hibernate-validator</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.apache.tomcat</groupId>
      <artifactId>tomcat-jdbc</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['hibernate-gorm'])

        then:
        ctx.configuration.containsKey("dataSource.url")
        ctx.configuration.containsKey("dataSource.driver-class-name")
        ctx.configuration.containsKey("hibernate.hbm2ddl.auto")
        ctx.configuration.containsKey("hibernate.cache.queries")
    }
}