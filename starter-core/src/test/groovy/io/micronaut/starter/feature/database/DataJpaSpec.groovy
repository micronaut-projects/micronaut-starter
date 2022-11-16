package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Issue

class DataJpaSpec extends ApplicationContextSpec implements CommandOutputFixture {

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

    void "test config for #features and #buildTool"(List<String> features, String dialect, BuildTool buildTool) {
        given:
        Options options = new Options(null, TestFramework.DEFAULT_OPTION, buildTool)

        when:
        GeneratorContext ctx = buildGeneratorContext(features, options)

        then:
        if (features.size() == 1) {
            assert ctx.configuration.containsKey("datasources.default.url")
        }
        ctx.configuration.get("jpa.default.properties.hibernate.hbm2ddl.auto") == Hbm2ddlAuto.UPDATE.toString()
        ctx.configuration.get("datasources.default.dialect") == dialect
        !ctx.configuration.containsKey("datasources.default.schema-generate")

        where:
        buildTool                 | features                  | dialect
        BuildTool.GRADLE_KOTLIN   | ['data-jpa']              | "H2"
        BuildTool.GRADLE          | ['data-jpa']              | "H2"
        BuildTool.MAVEN           | ['data-jpa']              | "H2"
        BuildTool.GRADLE_KOTLIN   | ['data-jpa', 'postgres']  | "POSTGRES"
        BuildTool.GRADLE          | ['data-jpa', 'postgres']  | "POSTGRES"
        BuildTool.MAVEN           | ['data-jpa', 'postgres']  | "POSTGRES"
        BuildTool.GRADLE_KOTLIN   | ['data-jpa', 'mysql']     | "MYSQL"
        BuildTool.GRADLE          | ['data-jpa', 'mysql']     | "MYSQL"
        BuildTool.MAVEN           | ['data-jpa', 'mysql']     | "MYSQL"
        BuildTool.GRADLE_KOTLIN   | ['data-jpa', 'mariadb']   | "MYSQL"
        BuildTool.GRADLE          | ['data-jpa', 'mariadb']   | "MYSQL"
        BuildTool.MAVEN           | ['data-jpa', 'mariadb']   | "MYSQL"
        BuildTool.GRADLE_KOTLIN   | ['data-jpa', 'oracle']    | "ORACLE"
        BuildTool.GRADLE          | ['data-jpa', 'oracle']    | "ORACLE"
        BuildTool.MAVEN           | ['data-jpa', 'oracle']    | "ORACLE"
        BuildTool.GRADLE_KOTLIN   | ['data-jpa', 'sqlserver'] | "SQL_SERVER"
        BuildTool.GRADLE          | ['data-jpa', 'sqlserver'] | "SQL_SERVER"
        BuildTool.MAVEN           | ['data-jpa', 'sqlserver'] | "SQL_SERVER"
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

    void 'feature data-jdbc contains correct hbm2ddl.auto values'(List<String> features, String prod, String test) {
        when:
        def output = generate(features)
        def appConfig = output["src/main/resources/application.yml"]
        def testConfig = output["src/test/resources/application-test.yml"]

        then:
        appConfig
        appConfig.contains(prod)
        !appConfig.contains("schema-generate")
        testConfig
        testConfig.contains(test)
        !testConfig.contains("schema-generate")

        where:
        features                  | prod            | test
        ['data-jpa']              | 'auto: update'  | 'auto: create-drop'
        ['data-jpa', 'flyway']    | "auto: none"    | 'auto: none'
        ['data-jpa', 'liquibase'] | "auto: none"    | 'auto: none'

    }
}
