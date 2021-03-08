package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Issue

class DataJdbcSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-jdbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc")
    }

    void "test data jdbc features"() {
        when:
        Features features = getFeatures(['data-jdbc'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("data-jdbc")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-jdbc"])
                .render()

        then:
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-jdbc")')
        template.contains('implementation("io.micronaut.sql:micronaut-jdbc-hikari")')
        template.contains('runtimeOnly("com.h2database:h2")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['data-jdbc'])
                .render()

        then:
        //src/main
        template.contains('''
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''')
        template.contains('''
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-jdbc</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''
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

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['data-jdbc'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "H2"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'postgres'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "POSTGRES"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'mysql'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'mariadb'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "MYSQL"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'oracle'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "ORACLE"

        when:
        ctx = buildGeneratorContext(['data-jdbc', 'sqlserver'])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.get("datasources.default.dialect") == "SQL_SERVER"
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/686")
    void 'test data-processor dependency is in provided scope for Groovy and Maven'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
            .language(Language.GROOVY)
            .features(["data-jdbc"])
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
