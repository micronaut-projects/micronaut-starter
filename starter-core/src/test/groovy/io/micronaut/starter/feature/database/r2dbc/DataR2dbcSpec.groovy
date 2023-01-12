package io.micronaut.starter.feature.database.r2dbc

import groovy.xml.XmlParser
import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.H2
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.feature.migration.Flyway
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

    void "test dependencies are present for gradle with #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataR2dbc.NAME, feature.name])
                .render()

        def jdbcDriver = renderDependency(feature.javaClientDependency.get().build())
        def r2dbcDriver = renderDependency(feature.r2DbcDependency.get().build())

        then: 'test-resources is applied for all but H2'
        template.contains('id("io.micronaut.test-resources") version') == isNotH2

        and: 'the processor and correct version of micronaut-data-r2dbc is applied'
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        template.contains('implementation("io.micronaut.data:micronaut-data-r2dbc")')
        !template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')

        and: 'the r2dbc driver is applied'
        template.contains($/runtimeOnly("$r2dbcDriver")/$)

        and: 'for test resources, the JDBC driver is applied to the test-resources service unless it is H2'
        template.contains($/testResourcesService("$jdbcDriver")/$) == isNotH2

        and: 'the jdbc driver is not applied'
        !template.contains($/runtimeOnly("$jdbcDriver")/$)

        where:
        db << [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
        isNotH2 = db != H2
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

    void "test dependencies are present for maven and H2"() {
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

    void "test dependencies are present for maven and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataR2dbc.NAME, db.NAME])
                .render()
        def project = new XmlParser().parseText(template)
        def micronautPlugin = project.build.plugins.plugin.find { it.artifactId.text() == 'micronaut-maven-plugin' }
        def testResourcesModuleName = feature.dbType.get().r2dbcTestResourcesModuleName
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

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
        with(project.dependencies.dependency.find { it.artifactId.text() == r2dbcDriverDependency.artifactId }) {
            scope.text() == 'runtime'
            groupId.text() == r2dbcDriverDependency.groupId
        }
        !project.dependencies.dependency.find { it.artifactId.text() == "h2" }

        jdbcFeature.name == 'jdbc-hikari'
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-jdbc-hikari" }

        with(micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == "io.micronaut.testresources" }) {
            it.artifactId.text() == "micronaut-test-resources-$testResourcesModuleName"
        }
        with(micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == jdbcDriverDependency.groupId }) {
            it.artifactId.text() == jdbcDriverDependency.artifactId
        }

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

    void "test migration dependencies are present for maven and H2"() {
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

    void "test migration dependencies are present for maven and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([DataR2dbc.NAME, Flyway.NAME, db.NAME])
                .render()
        def project = new XmlParser().parseText(template)
        def micronautPlugin = project.build.plugins.plugin.find { it.artifactId.text() == 'micronaut-maven-plugin' }
        def testResourcesModuleName = feature.dbType.get().r2dbcTestResourcesModuleName
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

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
        with(project.dependencies.dependency.find { it.artifactId.text() == r2dbcDriverDependency.artifactId }) {
            scope.text() == 'runtime'
            groupId.text() == r2dbcDriverDependency.groupId
        }
        with(project.dependencies.dependency.find { it.artifactId.text() == jdbcDriverDependency.artifactId }) {
            scope.text() == 'runtime'
            groupId.text() == jdbcDriverDependency.groupId
        }

        with(micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == "io.micronaut.testresources" }) {
            it.artifactId.text() == "micronaut-test-resources-$testResourcesModuleName"
        }

        and: 'We do not add the jdbc driver to the test resources dependencies as its already a dependency'
        !micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == jdbcDriverDependency.groupId }

        jdbcFeature.name == 'jdbc-hikari'
        !project.dependencies.dependency.find { it.artifactId.text() == "micronaut-jdbc-hikari" }

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "micronaut.data.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

    @Unroll
    void "test config #driver and build #buildTool"(BuildTool buildTool, Class<DatabaseDriverFeature> featureClass) {
        given:
        Options options = new Options(null, null, buildTool)
        GeneratorContext ctx = buildGeneratorContext([DataR2dbc.NAME, featureClass.NAME], options)
        def feature = ctx.getRequiredFeature(featureClass)
        def dialect = feature.dataDialect

        expect: 'the URL is only applied for H2, as otherwise test-resources will provide it'
        ctx.configuration.containsKey("r2dbc.datasources.default.url") == isH2

        and: 'dialect should always be set'
        ctx.configuration.get("r2dbc.datasources.default.dialect") == dialect

        and: 'db-type should be set for non-h2 databases'
        if (isH2) {
            assert ctx.configuration.get("r2dbc.datasources.default.db-type") == null
        } else {
            assert ctx.configuration.get("r2dbc.datasources.default.db-type") == feature.dbType.get().toString()
        }

        where:
        [buildTool, featureClass] << [BuildTool.values(), [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]].combinations()
        driver = featureClass.simpleName
        isH2 = featureClass == H2
    }
}
