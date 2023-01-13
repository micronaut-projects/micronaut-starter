package io.micronaut.starter.feature.database.r2dbc

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.feature.database.H2
import io.micronaut.starter.feature.database.MariaDB
import io.micronaut.starter.feature.database.MySQL
import io.micronaut.starter.feature.database.Oracle
import io.micronaut.starter.feature.database.PostgreSQL
import io.micronaut.starter.feature.database.SQLServer
import io.micronaut.starter.feature.database.jdbc.JdbcFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options

class R2dbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test dependencies are present for gradle with #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)

        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([R2dbc.NAME, feature.name])
                .render()

        def jdbcDriver = renderDependency(feature.javaClientDependency.get().build())
        def r2dbcDriver = renderDependency(feature.r2DbcDependency.get().build())

        then: 'test-resources is applied for all but H2'
        template.contains('id("io.micronaut.test-resources") version') == isNotH2

        and: 'the data processor is not applied'
        !template.contains('annotationProcessor("io.micronaut.data:micronaut-data-processor")')
        !template.contains('implementation("io.micronaut.data:micronaut-data-r2dbc")')
        template.contains('implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")')

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

    void "test maven r2dbc feature #featureClassName"(Class<DatabaseDriverFeature> driver) {
        when:
        def feature = beanContext.getBean(driver)

        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["r2dbc", driver.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

        then:
        jdbcFeature.name == 'jdbc-hikari'

        verifier.hasDependency('io.micronaut.r2dbc', 'micronaut-r2dbc-core', 'compile' )
        !verifier.hasDependency('micronaut-jdbc-hikari')
        verifier.hasDependency(r2dbcDriverDependency.groupId, r2dbcDriverDependency.artifactId, 'runtime')

        where:
        driver << [H2, PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = driver.simpleName

    }

    void "test dependencies are present for maven and #featureClassName"(Class<DatabaseDriverFeature> db) {
        when:
        def feature = beanContext.getBean(db)
        JdbcFeature jdbcFeature = beanContext.getBean(JdbcFeature)

        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .features([R2dbc.NAME, db.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)
        def testResourcesModuleName = feature.dbType.get().r2dbcTestResourcesModuleName
        def jdbcDriverDependency = feature.javaClientDependency.get().build()
        def r2dbcDriverDependency = feature.r2DbcDependency.get().build()

        then:
        !verifier.hasAnnotationProcessor("io.micronaut.data", "micronaut-data-processor")
        !verifier.hasDependency( "micronaut-data-r2dbc")
        verifier.hasDependency('io.micronaut.r2dbc', "micronaut-r2dbc-core", 'compile')
        verifier.hasDependency(r2dbcDriverDependency.groupId, r2dbcDriverDependency.artifactId, 'runtime')
        !verifier.hasDependency("h2")

        jdbcFeature.name == 'jdbc-hikari'
        !verifier.hasDependency("micronaut-jdbc-hikari")
        verifier.hasTestResourceDependency("micronaut-test-resources-$testResourcesModuleName")
        verifier.hasTestResourceDependency(jdbcDriverDependency.groupId, jdbcDriverDependency.artifactId)

        where:
        db << [PostgreSQL, MySQL, MariaDB, Oracle, SQLServer]
        featureClassName = db.simpleName
    }

    void "test config #driver and build #buildTool"(BuildTool buildTool, Class<DatabaseDriverFeature> featureClass) {
        given:
        Options options = new Options(null, null, buildTool)
        GeneratorContext ctx = buildGeneratorContext([R2dbc.NAME, featureClass.NAME], options)
        def feature = ctx.getRequiredFeature(featureClass)

        expect: 'the URL is only applied for H2, as otherwise test-resources will provide it'
        ctx.configuration.containsKey("r2dbc.datasources.default.url") == isH2

        and: 'dialect is not set'
        !ctx.configuration.containsKey("r2dbc.datasources.default.dialect")

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
