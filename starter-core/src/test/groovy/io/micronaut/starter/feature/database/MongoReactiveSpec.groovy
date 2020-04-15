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

class MongoReactiveSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    void "test mongo reactive features"() {
        when:
        Features features = getFeatures(['mongo-reactive'])

        then:
        features.contains("mongo-reactive")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(["mongo-reactive"])).render().toString()

        then:
        template.contains("implementation \"io.micronaut.configuration:micronaut-mongo-reactive\"")
        template.contains("testImplementation \"de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.0.1\"")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(buildProject(), getFeatures(["mongo-reactive"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-mongo-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>de.flapdoodle.embed</groupId>
      <artifactId>de.flapdoodle.embed.mongo</artifactId>
      <version>2.0.1</version>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        CommandContext ctx = buildCommandContext(['mongo-reactive'])

        then:
        ctx.getConfiguration().get("mongodb.uri") == "mongodb://\${MONGO_HOST:localhost}:\${MONGO_PORT:27017}"
    }
}
