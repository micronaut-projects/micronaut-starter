package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class MongoSpec extends BeanContextSpec {

    void "test mongo sync features"() {
        when:
        Features features = getFeatures(['mongo-sync'])

        then:
        features.contains("mongo-sync")
    }

    void "test mongo sync dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-sync"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync")')
        template.contains("testImplementation(\"de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.0.1\")")
    }

    void "test mongo sync dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-sync"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.mongodb</groupId>
      <artifactId>micronaut-mongo-sync</artifactId>
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

    void "test mongo reactive features"() {
        when:
        Features features = getFeatures(['mongo-reactive'])

        then:
        features.contains("mongo-reactive")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-reactive"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        template.contains("testImplementation(\"de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.0.1\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-reactive"]), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.mongodb</groupId>
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
        GeneratorContext ctx = buildGeneratorContext(['mongo-reactive'])

        then:
        ctx.getConfiguration().get("mongodb.uri") == "mongodb://\${MONGO_HOST:localhost}:\${MONGO_PORT:27017}"
    }
}
