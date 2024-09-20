package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.TestFramework

import java.util.stream.Collectors

class TestContainersSpec extends ApplicationContextSpec {

    void "test oracle dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'oracle'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:oracle-xe")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test mysql dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'mysql'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:mysql")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test postgres dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'postgres'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:postgresql")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test mariadb dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'mariadb'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:mariadb")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test sqlserver dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'sqlserver'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:mssqlserver")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test mongo-reactive dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'mongo-reactive'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:mongodb")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test mongo-sync dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'mongo-sync'])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:mongodb")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test neo4j dependency is present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, Neo4jBolt.NAME])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:neo4j")')
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "test testcontainers core is present when no testcontainer modules are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME])
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:testcontainers")')
    }

    void "testframework dependency is present for gradle for feature #feature and spock framework"() {
        when:
        def template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, feature])
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:spock")')

        where:
        feature << ["mongo-reactive", "mongo-sync", "mysql", "postgres", "mariadb", "sqlserver", "oracle"]
    }

    void "testframework dependency is present for gradle for feature #feature and junit framework"() {

        when:
        def template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, feature])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.testcontainers:junit-jupiter")')

        where:
        feature << ["mongo-reactive", "mongo-sync", "mysql", "postgres", "mariadb", "sqlserver", "oracle"]
    }

    void "test oracle dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'oracle'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>oracle-xe</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test mysql dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'mysql'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mysql</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test postgres dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'postgres'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test mariadb dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'mariadb'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mariadb</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test sqlserver dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'sqlserver'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mssqlserver</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test mongo-reactive dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'mongo-reactive'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mongodb</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test mongo-sync dependency is present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'mongo-sync'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mongodb</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test testcontainers dependency is present and no testcontainer modules are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "testframework dependency is present for maven for feature #feature and spock framework"() {
        when:
        def template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, feature])
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>spock</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        feature << ["mongo-reactive", "mongo-sync", "mysql", "postgres", "mariadb", "sqlserver", "oracle"]
    }

    void "testframework dependency is present for maven for feature #feature and junit framework"() {

        when:
        def template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, feature])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>junit-jupiter</artifactId>
      <scope>test</scope>
    </dependency>
""")

        where:
        feature << ["mongo-reactive", "mongo-sync", "mysql", "postgres", "mariadb", "sqlserver", "oracle"]
    }

    void "test there is a dependency for every non embedded driver feature"() {
        when:
        String mavenTemplate = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([TestContainers.NAME, driverFeature.getName()])
                .render()

        String gradleTemplate = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features([TestContainers.NAME, driverFeature.getName()])
                .render()

        then:
        gradleTemplate.contains("org.testcontainers")
        mavenTemplate.contains("org.testcontainers")

        where:
        driverFeature <<  beanContext.streamOfType(DatabaseDriverFeature)
                .filter({ f ->  !f.embedded() })
                .collect(Collectors.toList())
    }

    void "test all non embedded drivers apply the test containers feature for Maven"() {
        when:
        Features features = getFeatures([TestContainers.NAME, driverFeature.getName()],
                null,
                null,
                BuildTool.MAVEN)

        then:
        features.contains("testcontainers")

        where:
        driverFeature <<  beanContext.streamOfType(DatabaseDriverFeature)
                .filter(f -> f.name != "oracle-cloud-atp")
                .filter({ f ->  !f.embedded() })
                .collect(Collectors.toList())
    }

    void "test cassandra dependency is present for #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([TestContainers.NAME, Cassandra.NAME])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.testcontainers", "cassandra", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }
}
