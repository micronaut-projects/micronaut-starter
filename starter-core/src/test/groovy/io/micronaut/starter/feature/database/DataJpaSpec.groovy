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

class DataJpaSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void "test data jpa features"() {
        when:
        Features features = getFeatures(['data-jpa'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-tomcat")
        features.contains("data-jpa")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["data-jpa"])).render().toString()

        then:
        template.contains("annotationProcessor \"io.micronaut.data:micronaut-data-processor\"")
        template.contains("implementation \"io.micronaut.data:micronaut-data-jpa\"")
        template.contains("implementation \"io.micronaut.configuration:micronaut-jdbc-tomcat\"")
        template.contains("runtimeOnly \"com.h2database:h2\"")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["data-jpa"]), []).render().toString()

        then:
        //src/main
        template.contains("""
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>\${micronaut.data.version}</version>
            </path>
""")
        //src/test
        template.contains("""
                <path>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-data-processor</artifactId>
                  <version>\${micronaut.data.version}</version>
                </path>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-jdbc-tomcat</artifactId>
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
        CommandContext ctx = buildCommandContext(['data-jpa'])
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ctx.getTemplates().get("yamlConfig").write(baos)

        then:
        baos.toString().contains("""
datasources:
  default:
    url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    driverClassName: org.h2.Driver
    username: sa
    password: ''
    schema-generate: CREATE_DROP
    dialect: H2
jpa.default.properties.hibernate.hbm2ddl.auto: update    
""".trim())
    }
}
