package io.micronaut.starter.feature.database.r2dbc

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.database.DataJdbcSpec
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.H2
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.feature.migration.Flyway
import io.micronaut.starter.feature.migration.Liquibase
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import spock.lang.Shared

class DataR2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['data-r2dbc'])
        String readme = output["README.md"]

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

    void "check validator with TestContainers and #migrator"() {
        when:
        generate([DataR2dbc.NAME, migrator, TestContainers.NAME])

        then:
        thrown(IllegalArgumentException)

        where:
        migrator << [Flyway.NAME, Liquibase.NAME]
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
        features.contains("jdbc-hikari")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-r2dbc"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        then:
        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-processor', Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-r2dbc')
        !verifier.hasDependency('io.micronaut.r2dbc', 'micronaut-r2dbc-core')
        verifier.hasDependency('io.r2dbc', 'r2dbc-h2', Scope.RUNTIME)
        !verifier.hasDependency('com.h2database', 'h2', Scope.RUNTIME)
        !verifier.hasDependency('io.micronaut.sql', 'micronaut-jdbc-hikari')
    }

    void "test dependencies are present for gradle with TestContainers and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataR2dbc.NAME, feature.name, TestContainers.NAME])
                .render()
        def jdbcDriver = feature.javaClientDependency.get().build()
        def r2dbcDriver = feature.r2DbcDependency.get().build()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        then:
        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-processor', Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-r2dbc')
        !verifier.hasDependency('io.micronaut.r2dbc', 'micronaut-r2dbc-core')
        !verifier.hasDependency('io.micronaut.sql', 'micronaut-jdbc-hikari')

        and: 'we have the r2dbc driver for runtime and the jdbc driver for TestContainers to check the container is up under test'
        verifier.hasDependency(r2dbcDriver.groupId, r2dbcDriver.artifactId, Scope.RUNTIME)
        verifier.hasDependency(jdbcDriver.groupId, jdbcDriver.artifactId, Scope.TEST_RUNTIME)

        and: 'has the required TestContainers dependencies'
        verifier.hasDependency('org.testcontainers', 'r2dbc', Scope.TEST)
        verifier.hasDependency('org.testcontainers', 'testcontainers', Scope.TEST)

        and: 'the db dependency as required by TestContainers for all but H2 obviously'
        isH2 || verifier.hasDependency('org.testcontainers', TestContainers.artifactIdForDriverFeature(feature).get(), Scope.TEST)

        where:
        db << [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
        isH2 = db == H2
    }

    void "test dependencies are present for gradle with #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([DataR2dbc.NAME, feature.name])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)

        def jdbcDriver = feature.javaClientDependency.get().build()
        def r2dbcDriver = feature.r2DbcDependency.get().build()

        then: 'test-resources is applied for all but H2'
        isH2 || template.contains('id("io.micronaut.test-resources") version')

        and: 'the processor and correct version of micronaut-data-r2dbc is applied'
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-processor', Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-r2dbc')
        !verifier.hasDependency('io.micronaut.r2dbc', 'micronaut-r2dbc-core')

        and: 'the r2dbc driver is applied'
        verifier.hasDependency(r2dbcDriver.groupId, r2dbcDriver.artifactId, Scope.RUNTIME)

        and: 'for test resources, the JDBC driver is applied to the test-resources service unless it is H2'
        isH2 || verifier.hasDependency(jdbcDriver.groupId, jdbcDriver.artifactId, Scope.TEST_RESOURCES_SERVICE)

        and: 'the jdbc driver is not applied'
        !verifier.hasDependency(jdbcDriver.artifactId, jdbcDriver.groupId, Scope.RUNTIME)

        where:
        db << [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
        isH2 = db == H2
    }

    void "test migration dependencies are present for #buildTool and H2"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["data-r2dbc", "flyway"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency("io.micronaut.data", "micronaut-data-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-r2dbc", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut.r2dbc", "micronaut-r2dbc-core", Scope.COMPILE)
        verifier.hasDependency("io.r2dbc", "r2dbc-h2", Scope.RUNTIME)
        verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME)
        verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-hikari", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void "test migration dependencies are present for gradle and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-r2dbc", feature.name, "flyway"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.GRADLE, template)
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

        then:
        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-processor', Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('io.micronaut.data', 'micronaut-data-r2dbc')
        !verifier.hasDependency('io.micronaut.r2dbc', 'micronaut-r2dbc-core')
        verifier.hasDependency(r2dbcDriverDependency.groupId, r2dbcDriverDependency.artifactId, Scope.RUNTIME)
        verifier.hasDependency('io.micronaut.sql', 'micronaut-jdbc-hikari')
        verifier.hasDependency(jdbcDriverDependency.groupId, jdbcDriverDependency.artifactId, Scope.RUNTIME)

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

    void "test dependencies are present for maven and H2"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["data-r2dbc"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.data", "micronaut-data-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-r2dbc", Scope.COMPILE)
        !verifier.hasDependency("micronaut-r2dbc-core")
        verifier.hasDependency('io.r2dbc', "r2dbc-h2", Scope.RUNTIME)
        !verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME)
        jdbcFeature.name == 'jdbc-hikari'
        !verifier.hasDependency("io.micronaut.sql","micronaut-jdbc-hikari")
        if (buildTool == BuildTool.MAVEN) {
            DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)
        }

        where:
        buildTool << BuildTool.values()
    }

    void "test dependencies are present for maven and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features([DataR2dbc.NAME, db.NAME])
                .render()
        def testResourcesModuleName = feature.dbType.get().r2dbcTestResourcesModuleName
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.data", "micronaut-data-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-r2dbc", "compile")
        !verifier.hasDependency("micronaut-r2dbc-core")
        verifier.hasDependency(r2dbcDriverDependency.groupId, r2dbcDriverDependency.artifactId, "runtime")
        !verifier.hasDependency("h2")

        jdbcFeature.name == 'jdbc-hikari'
        !verifier.hasDependency("micronaut-jdbc-hikari")

        verifier.hasTestResourceDependency("micronaut-test-resources-$testResourcesModuleName")
        verifier.hasTestResourceDependency(jdbcDriverDependency.groupId, jdbcDriverDependency.artifactId)
        DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

    void "test migration dependencies are present for maven and H2"() {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["data-r2dbc", "flyway"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        //src/main
        verifier.hasDependency("io.micronaut.data", "micronaut-data-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('io.micronaut.data', "micronaut-data-r2dbc", Scope.COMPILE)
        !verifier.hasDependency("micronaut-r2dbc-core")
        verifier.hasDependency('io.r2dbc', "r2dbc-h2", Scope.RUNTIME)
        verifier.hasDependency('com.h2database',"h2", Scope.RUNTIME)

        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency("micronaut-jdbc-hikari")
        and:
        DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)
    }

    void "test migration dependencies are present for maven and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features([DataR2dbc.NAME, Flyway.NAME, db.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)
        def testResourcesModuleName = feature.dbType.get().r2dbcTestResourcesModuleName
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

        then:
        verifier.hasDependency("io.micronaut.data", "micronaut-data-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-r2dbc", "compile")

        !verifier.hasDependency("micronaut-r2dbc-core")
        verifier.hasDependency(r2dbcDriverDependency.groupId, r2dbcDriverDependency.artifactId, "runtime")
        verifier.hasDependency(jdbcDriverDependency.groupId, jdbcDriverDependency.artifactId, "runtime")
        verifier.hasTestResourceDependency("io.micronaut.testresources", "micronaut-test-resources-$testResourcesModuleName")

        and: 'The JDBC driver is added as a runtime dependency for migration'
        verifier.hasDependency(jdbcDriverDependency.groupId, jdbcDriverDependency.artifactId, Scope.RUNTIME)

        and: 'The hikari pool is added for the migration'
        jdbcFeature.name == 'jdbc-hikari'
        verifier.hasDependency("micronaut-jdbc-hikari")
        DataJdbcSpec.assertTemplateDoesNotContainMicronautDataVersionProperty(template)

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

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
