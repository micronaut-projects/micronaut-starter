package io.micronaut.starter.feature.database

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CommandContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class HibernateGormSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

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
        String template = buildGradle.template(buildProject(), getFeatures(["hibernate-gorm"])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-hibernate-gorm\"")
        template.contains("implementation \"io.micronaut.configuration:micronaut-hibernate-validator\"")
        template.contains("runtimeOnly \"com.h2database:h2\"")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["hibernate-gorm"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-hibernate-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
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
    }

    void "test config"() {
        when:
        CommandContext ctx = buildCommandContext(['hibernate-gorm'])
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ctx.getTemplates().get("yamlConfig").write(baos)

        then:
        baos.toString().contains("""
dataSource:
  url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
  pooled: true
  jmxExport: true
  driverClassName: org.h2.Driver
  username: sa
  password: ''
hibernate:
  hbm2ddl:
    auto: update
  cache:
    queries: false
    use_second_level_cache: false
    use_query_cache: false    
""".trim())
    }
}