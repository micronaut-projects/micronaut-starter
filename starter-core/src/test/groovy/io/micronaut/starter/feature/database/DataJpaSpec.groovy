package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue

class DataJpaSpec extends ApplicationContextSpec {

    void "test data jpa features"() {
        when:
        Features features = getFeatures(['data-jpa'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("data-jpa")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-jpa"])
                .render()

        then:
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-hibernate-jpa")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
    }

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-jpa"])
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('id("org.jetbrains.kotlin.plugin.jpa")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['data-jpa'])
                .render()

        then:
        //src/main
        template.contains('''\
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''')
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''\
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''\
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
''')

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()
    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['data-jpa'])
                .language(Language.KOTLIN)
                .render()

        then:
        //src/main
        template.contains('''
          <compilerPlugins>
            <plugin>jpa</plugin>
            <plugin>all-open</plugin>
          </compilerPlugins>
''')
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['data-jpa'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.containsKey("jpa.default.properties.hibernate.hbm2ddl.auto")
        ctx.configuration.get("datasources.default.dialect") == "H2"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'postgres'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "POSTGRES"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'mysql'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'mariadb'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'oracle'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "ORACLE"

        when:
        ctx = buildGeneratorContext(['data-jpa', 'sqlserver'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "SQL_SERVER"
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
            .language(Language.GROOVY)
            .features(["data-jpa"])
            .render()

        then:
        template.contains('''
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-processor</artifactId>
      <scope>provided</scope>
    </dependency>
''')
    }
}
