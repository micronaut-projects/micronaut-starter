package io.micronaut.starter.feature.database

import io.micronaut.context.BeanContext
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class Neo4jGormSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void "test neo4j gorm features"() {
        when:
        Features features = getFeatures(['neo4j-gorm'])

        then:
        features.contains("groovy")
        features.contains("neo4j-bolt")
        features.contains("neo4j-gorm")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["neo4j-gorm"])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-neo4j-gorm\"")
        template.contains("implementation \"io.micronaut.configuration:micronaut-neo4j-bolt\"")
        template.contains("testRuntime \"org.neo4j.test:neo4j-harness\"")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["neo4j-gorm"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-neo4j-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-neo4j-bolt</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.neo4j.test</groupId>
      <artifactId>neo4j-harness</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildCommandContext(['neo4j-gorm'])

        then:
        ctx.getConfiguration().get("neo4j.uri") == "bolt://\${NEO4J_HOST:localhost}"
    }
}
