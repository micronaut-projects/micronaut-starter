package io.micronaut.starter.feature.database.r2dbc

import groovy.xml.XmlParser
import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import spock.lang.Shared
import spock.lang.Unroll

class DataR2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-r2dbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-r2dbc/latest/guide/")
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/")
    }

    void "test data r2dbc features"() {
        when:
        Features features = getFeatures(['data-r2dbc'])

        then:
        features.contains("data")
        features.contains("h2")
        features.contains("r2dbc")
        features.contains("data-r2dbc")
        !features.contains("jdbc-hikari")
    }

    void "test data r2dbc features with migration"() {
        when:
        Features features = getFeatures(['data-r2dbc', 'flyway'])

        then:
        features.contains("flyway")
        features.contains("data")
        features.contains("h2")
        features.contains("r2dbc")
        features.contains("data-r2dbc")
        !features.contains("jdbc-hikari")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-r2dbc"])
                .render()

        then:
        jdbcFeature.name == 'jdbc-hikari'
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-r2dbc")')
        !template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')
        template.contains("runtimeOnly(\"io.r2dbc:r2dbc-h2\")")
        !template.contains("""runtimeOnly("com.h2database:h2")""")
        !template.contains("implementation(\"io.micronaut.sql:micronaut-jdbc-hikari\")")
    }

    void "test migration dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-r2dbc", "flyway"])
                .render()

        then:
        jdbcFeature.name == 'jdbc-hikari'
        template.contains("annotationProcessor(\"io.micronaut.data:micronaut-data-processor\")")
        template.contains('implementation("io.micronaut.data:micronaut-data-r2dbc")')
        !template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')
        template.contains("runtimeOnly(\"io.r2dbc:r2dbc-h2\")")
        template.contains("""runtimeOnly("com.h2database:h2")""")
        !template.contains("implementation(\"io.micronaut.sql:micronaut-jdbc-hikari\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["data-r2dbc"])
                .render()
        def project = new XmlParser().parseText(template)

        then:
        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
            artifacts.contains("io.micronaut.data:micronaut-data-processor")
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-data-r2dbc" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.data'
        }
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-r2dbc-core" }
        with(project.dependencies.dependency.find { it.artifactId.text() == "r2dbc-h2" }) {
            scope.text() == 'runtime'
            groupId.text() == 'io.r2dbc'
        }
        !project.dependencies.dependency.find { it.artifactId.text() == "h2" }

        jdbcFeature.name == 'jdbc-hikari'
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-jdbc-hikari" }

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()
    }
    
    void "test migration dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["data-r2dbc", "flyway"])
                .render()
        def project = new XmlParser().parseText(template)

        then:
        //src/main
        with(project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }) {
            def artifacts = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
            artifacts.contains("io.micronaut.data:micronaut-data-processor")
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == "micronaut-data-r2dbc" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.data'
        }
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-r2dbc-core" }
        with(project.dependencies.dependency.find { it.artifactId.text() == "r2dbc-h2" }) {
            scope.text() == 'runtime'
            groupId.text() == 'io.r2dbc'
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == "h2" }) {
            scope.text() == 'runtime'
            groupId.text() == 'com.h2database'
        }

        jdbcFeature.name == 'jdbc-hikari'
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-jdbc-hikari" }

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()
    }

    @Unroll
    void "test config #driver and #dialect adn build #buildTool"(BuildTool buildTool) {
        given:
        Options options = new Options(null, null, buildTool)
        GeneratorContext ctx = buildGeneratorContext([DataR2dbc.NAME, driver], options)

        expect:
        ctx.configuration.containsKey("r2dbc.datasources.default.url")
        ctx.configuration.get("r2dbc.datasources.default.dialect") == dialect

        where:
        buildTool               | driver      | dialect
        BuildTool.MAVEN         | "h2"        | "H2"
        BuildTool.GRADLE_KOTLIN | "h2"        | "H2"
        BuildTool.GRADLE        | "h2"        | "H2"
        BuildTool.MAVEN         | "postgres"  | "POSTGRES"
        BuildTool.GRADLE_KOTLIN | "postgres"  | "POSTGRES"
        BuildTool.GRADLE        | "postgres"  | "POSTGRES"
        BuildTool.MAVEN         | "mysql"     | "MYSQL"
        BuildTool.GRADLE_KOTLIN | "mysql"     | "MYSQL"
        BuildTool.GRADLE        | "mysql"     | "MYSQL"
        BuildTool.MAVEN         | "mariadb"   | "MYSQL"
        BuildTool.GRADLE_KOTLIN | "mariadb"   | "MYSQL"
        BuildTool.GRADLE        | "mariadb"   | "MYSQL"
        BuildTool.MAVEN         | "sqlserver" | "SQL_SERVER"
        BuildTool.GRADLE_KOTLIN | "sqlserver" | "SQL_SERVER"
        BuildTool.GRADLE        | "sqlserver" | "SQL_SERVER"
        BuildTool.MAVEN         | "oracle"    | "ORACLE"
        BuildTool.GRADLE_KOTLIN | "oracle"    | "ORACLE"
        BuildTool.GRADLE        | "oracle"    | "ORACLE"
    }
}
